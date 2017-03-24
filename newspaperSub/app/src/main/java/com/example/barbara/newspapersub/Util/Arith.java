package com.example.barbara.newspapersub.Util;

import java.math.BigDecimal;

/**
 * 工具
 */
public class Arith {

        /**
         * * 两个Double数相加 *
         *
         * @param v1 *
         * @param v2 *
         * @return Double
         */
        public static Double add(Double v1, Double v2) {
            BigDecimal b1 = new BigDecimal(v1.toString());
            BigDecimal b2 = new BigDecimal(v2.toString());
            return new Double(b1.add(b2).doubleValue());
        }

        /**
         * * 两个Double数相减 *
         *
         * @param v1 *
         * @param v2 *
         * @return Double
         */
        public static Double sub(Double v1, Double v2) {
            BigDecimal b1 = new BigDecimal(v1.toString());
            BigDecimal b2 = new BigDecimal(v2.toString());
            return new Double(b1.subtract(b2).doubleValue());
        }

    /**
     * 获取四位随机数
     * @return
     */
        public static int getRandom4() {
            return (int)(Math.random()*9000+1000);
        }
}
