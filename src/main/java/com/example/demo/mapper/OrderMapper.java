package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT id, req_hash FROM orders WHERE request_id = #{requestId} LIMIT 1")
    Order findByRequestId(@Param("requestId") String requestId);
    @Select("SELECT id, product_id, count, status FROM orders WHERE id = #{id} LIMIT 1")
    Order findForCancel(@Param("id") Long id);

    @org.apache.ibatis.annotations.Update("""
                                            UPDATE orders
                                            SET status = 1
                                            WHERE id = #{id} AND status = 0
                                            """)
    int cancelIfPending(@Param("id") Long id);
}