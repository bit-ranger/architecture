package com.rainyalley.architecture.service;

import com.rainyalley.architecture.dao.entity.UserDo;
import com.rainyalley.architecture.dao.mapper.UserMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootApplication
@Import(ServiceConfig.class)
@EnableCaching
public class ServiceTestConfig {

//    @Bean("userMapper")
//    @Primary
    public UserMapper mockUserMapper(){
        UserMapper userMapperMock = mock(UserMapper.class);
        List<UserDo> mockUserList = IntStream.range(1,10).mapToObj(this::makeUserDo).collect(Collectors.toList());
        when(userMapperMock.select(anyMapOf(String.class, Object.class))).thenReturn(mockUserList);
        when(userMapperMock.delete(any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                UserDo arg = (UserDo) invocation.getArguments()[0];
                Optional opt = getById(arg.getId(), mockUserList);
                if(opt.isPresent()){
                    mockUserList.remove(opt.get());
                    return 1;
                }
                return 0;
            }
        });
        when(userMapperMock.insert(any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                UserDo arg = (UserDo) invocation.getArguments()[0];
                Optional<UserDo> opt = getById(arg.getId(), mockUserList);
                if(opt.isPresent()){
                    throw new IllegalArgumentException("UserDo exists");
                } else {
                    mockUserList.add(opt.get());
                    return 1;
                }

            }
        });
        when(userMapperMock.update(any())).thenAnswer(new Answer<UserDo>() {
            public UserDo answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                UserDo arg = (UserDo) args[0];
                //Object mock = invocation.getMock();
                Optional<UserDo> opt = getById(arg.getId(), mockUserList);
                if(opt.isPresent()){
                    try {
                        PropertyUtils.copyProperties(opt.get(), arg);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                    return opt.get();
                } else {
                    throw new IllegalArgumentException("no UserDo");
                }

            }
        });

        return userMapperMock;
    }

    private UserDo makeUserDo(int id){
        UserDo userDo = new UserDo();
        userDo.setId(id);
        userDo.setName("name_" + id);
        userDo.setPassword("password_" + id);
        return userDo;
    }

    private Optional<UserDo> getById(Integer id, List<UserDo> list){
        return list.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

}
