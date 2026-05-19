package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * ClassName: ProductMapper
 * Package: com.example.demo.Mapper
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:25
 * @Version 1.0
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Update("""
        UPDATE product
        SET stock = stock - #{count}
        WHERE id = #{id}
          AND stock >= #{count}
        """)
    int deductStock(@Param("id") Long id, @Param("count") int count);

    @org.apache.ibatis.annotations.Update("""
        UPDATE product
        SET stock = stock + #{count}
        WHERE id = #{productId}
        """)
    int addStock(@Param("productId") Long productId, @Param("count") int count);
}