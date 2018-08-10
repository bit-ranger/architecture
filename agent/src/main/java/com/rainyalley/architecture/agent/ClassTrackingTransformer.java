package com.rainyalley.architecture.agent;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassTrackingTransformer implements ClassFileTransformer {

    private static final ClassPool pool = ClassPool.getDefault();

    static {
        try {
            pool.appendClassPath("D:\\Doc\\MyRepo\\architecture\\agent\\target\\classes");
            pool.appendClassPath("D:\\Doc\\MyRepo\\architecture\\agent\\target\\test-classes");
            pool.importPackage("com.rainyalley.architecture.agent");
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
        if(!className.startsWith("com.rainyalley.architecture.test")){
            return classfileBuffer;
        }

        try {
//            String[] names = className.split("\\.");
//            if (names != null && names.length > 0) {
//                int length = names.length;
//
//                for(int i = 0; i < length; ++i) {
//                    if (i != 0) {
//                        packagename.append(".");
//                    }
//
//                    packagename.append(names[i]);
//                    if (classList.contains(packagename.toString())) {
//                        flag = true;
//                        break;
//                    }
//                }
//            } else {
//                packagename.append(className);
//                if (classList.contains(packagename.toString())) {
//                    flag = true;
//                }
//            }

            CtClass ctclass = pool.get(className);

            if(ctclass.isInterface()){
                return classfileBuffer;
            }

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
                sb.append(String.format("RainyalleyArchitectureTimeTracking.track(\"%s\", startTime, endTime);", method.getLongName()));

                if (!"void".equals(returnType)) {
                    sb.append("return result ; \n");
                }

                sb.append("}");
                generatedMethod.setBody(sb.toString());
                ctclass.addMethod(generatedMethod);
            }

            return ctclass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return classfileBuffer;
        }
    }
}
