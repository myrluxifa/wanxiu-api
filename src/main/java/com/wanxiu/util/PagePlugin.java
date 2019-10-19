package com.wanxiu.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PagePlugin {
    public static Pageable pagePlugin(int page, int size) {
        if(page>0) {
            page=page-1;
        }
        if(size==0) {
            size=10;
        }
        return new PageRequest(page,size);
    }

    public static Pageable pagePluginSort(int page, int size, Sort.Direction direction, String... field ) {
        if(page>0) {
            page=page-1;
        }
        if(size==0) {
            size=10;
        }
        return new PageRequest(page,size,new Sort(direction,field));
    }

    public static int pageFromLimit(int page,int size) {

        if(page<=1) {
            page=0;
        }else {
            page=(page-1)*size;
        }
        return page;
    }

    public static int pageFromLimit(String _page,String _size) {
        int page=Integer.valueOf(_page);
        int size=Integer.valueOf(_size);
        if(page<=1) {
            page=0;
        }else {
            page=(page-1)*size;
        }
        return page;
    }
}
