package com.rainyalley.architecture.agent;

import javassist.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ClassTrackingTransformer implements ClassFileTransformer {

    private String javaAgentPrefix = "-javaagent:";

    private String delimiter = ";";

    private  ClassPool pool = ClassPool.getDefault();

    private List<String> classPathList = new ArrayList<>();
    private List<String> agentPackageList = new ArrayList<>();


    {
        try {
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            List<String> arguments = runtimeMxBean.getInputArguments();
            for (String arg : arguments) {
                if(!arg.startsWith(javaAgentPrefix)){
                    continue;
                }

                String agentJarPath = arg.substring(javaAgentPrefix.length());
                pool.appendClassPath(agentJarPath);
                String agentConfPath = agentJarPath.substring(0,agentJarPath.lastIndexOf(File.separator)) + File.separator + "conf.properties";
                Properties properties = new Properties();
                try {
                    File confFile = new File(agentConfPath);
                    if(!confFile.exists()){
                        continue;
                    }
                    properties.load(new BufferedReader(new FileReader(confFile)));
                    Object classpath = properties.get("classpath");
                    if(classpath != null && classpath.toString().length() > 0){
                        classPathList.addAll(Arrays.asList(classpath.toString().split(delimiter)));

                    }
                    Object agentPackage = properties.get("agent-package");
                    if(agentPackage != null && agentPackage.toString().length() > 0){
                        agentPackageList.addAll(Arrays.asList(agentPackage.toString().split(delimiter)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String classpath : classPathList) {
                pool.appendClassPath(classpath);
            }
            pool.importPackage("com.rainyalley.architecture.agent.runtime");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replace("/", ".");
        boolean inPackageList = false;
        for (String agentPackage : agentPackageList) {
            if(className.startsWith(agentPackage)){
                inPackageList = true;
                break;
            }
        }

        if(!inPackageList){
            return classfileBuffer;
        }


        try {
            CtClass ctclass = pool.get(className);

            if(ctclass.isInterface()){
                return classfileBuffer;
            }

            ctclass = resolveConstructor(ctclass);

            ctclass = resolveMethods(ctclass);

            return ctclass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return classfileBuffer;
        }
    }

    private CtClass resolveMethods(CtClass ctclass) throws CannotCompileException, NotFoundException {
        CtMethod[] declaredMethods = ctclass.getDeclaredMethods();

        for (CtMethod method : declaredMethods) {
            if (method.isEmpty()){
                continue;
            }
            String methodNameOriginal = method.getName();
            String methodNameImpl = method.getName()  + "$impl";
            method.setName(methodNameImpl);
            CtMethod generatedMethod = CtNewMethod.copy(method, methodNameOriginal, ctclass, (ClassMap)null);
            String returnType = method.getReturnType().getName();
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("long startTime = System.nanoTime();\n");
            if (!"void".equals(returnType)) {
                sb.append(returnType).append(" result = ");
            }

            sb.append(methodNameImpl).append("($$);\n");

            sb.append("long endTime = System.nanoTime();\n");
            sb.append(String.format("RainyalleyArchitectureTimeTracking.track(\"%s\", startTime, endTime);", generatedMethod.getLongName()));

            if (!"void".equals(returnType)) {
                sb.append("return result ; \n");
            }

            sb.append("}");
            generatedMethod.setBody(sb.toString());
            ctclass.addMethod(generatedMethod);
        }

        return ctclass;
    }

    private CtClass resolveConstructor(CtClass ctclass){
        //            CtConstructor[] constructors = ctclass.getConstructors();
//            String fieldName = "startTime4javassist";
//            CtField f = new CtField(CtClass.longType, fieldName, ctclass);
//            ctclass.addField(f);
//
//            for (CtConstructor constructor : constructors) {
//                StringBuffer outputStr = new StringBuffer("\n long cost = (endTime - " + fieldName + ") / 1000000;\n");
//                if (outputlevel == 3) {
//                    outputStr.append("org.slf4j.Logger logt = org.slf4j.LoggerFactory.getLogger(").append(className).append(".class);\n");
//                } else if (outputlevel == 2) {
//                    outputStr.append("org.apache.commons.logging.Log logt = org.apache.commons.logging.LogFactory.getLog(").append(className).append(".class);\n");
//                }
//
//                outputStr.append("if(cost >= " + timedisplay + "){");
//                if (outputlevel != 1) {
//                    outputStr.append("logt.error(\" ");
//                } else {
//                    outputStr.append("System.out.println(\" ");
//                }
//
//                outputStr.append(constructor.getLongName());
//                outputStr.append(" cost:\" + cost + \"ms.\");\n}\n");
//
//                constructor.insertBefore(fieldName + " = System.nanoTime();\n");
//                constructor.insertAfter("\nlong endTime = System.nanoTime();\n" + outputStr);
//            }
        return ctclass;
    }
}
