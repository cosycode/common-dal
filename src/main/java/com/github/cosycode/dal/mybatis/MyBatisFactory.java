package com.github.cosycode.dal.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2020/12/15 16:52
 **/
@Slf4j
public class MyBatisFactory {

    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")){
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            log.error("MyBatisFactory loading failure", e);
        }
    }

    private MyBatisFactory() {
    }

    /**
     * 开启 Session
     */
    public static SqlSession openSession() {
        return sqlSessionFactory.openSession();
    }

    /**
     * 以批量执行模式 ExecutorType.BATCH 开启 Session
     */
    public static SqlSession openBatchSession() {
        return sqlSessionFactory.openSession(ExecutorType.BATCH);
    }

    /**
     * @param bean        需要处理的对象
     * @param clazzMapper 需要处理的Mapper
     * @param exeFun Mapper对象处理实体类B的处理方法
     * @param <B>         list对象对应的实体类
     * @param <M>         实体类B对应处理的Mapper对象
     * @return    成功影响行数
     */
    public static <B, M> int execute(B bean, Class<M> clazzMapper, ToIntBiFunction<M, B> exeFun) {
        if (bean == null) {
            return 0;
        }
        try (SqlSession batchInstance = openBatchSession()) {
            M mapper = batchInstance.getMapper(clazzMapper);
            return exeFun.applyAsInt(mapper, bean);
        }
    }

    /**
     * @param list        需要处理的list对象
     * @param clazzMapper 需要处理的Mapper
     * @param batchExeFun Mapper对象处理实体类B的处理方法
     * @param <B>         list对象对应的实体类
     * @param <M>         实体类B对应处理的Mapper对象
     * @return    成功影响行数
     */
    public static <B, M> int batchExecute(List<B> list, Class<M> clazzMapper, ToIntBiFunction<M, B> batchExeFun) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        try (SqlSession batchInstance = openBatchSession()) {
            M mapper = batchInstance.getMapper(clazzMapper);
            return list.stream().mapToInt(it -> batchExeFun.applyAsInt(mapper, it)).sum();
        }
    }

    /**
     * @param consumer    处理函数
     */
    public static void batchExecute(Consumer<SqlSession> consumer) {
        try (SqlSession batchInstance = openBatchSession()) {
            consumer.accept(batchInstance);
        }
    }

}
