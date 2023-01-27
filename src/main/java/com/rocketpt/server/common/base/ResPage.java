package com.rocketpt.server.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页的返回值
 *
 * @author liyue
 * @version v1
 * @create 2019-03-07 16:48:42
 * @copyright www.liderong.cn
 */
@Getter
@Setter
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ResPage {

    private long total;

    private int page;

    private int size;

    public static ResPage getPage(long total, int page, int size) {
        return new ResPage(total, page, size);
    }

    public static ResPage defaultPage() {
        return new ResPage(10, 1, 10);
    }
}
