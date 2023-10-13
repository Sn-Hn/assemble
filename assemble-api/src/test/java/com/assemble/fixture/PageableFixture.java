package com.assemble.fixture;

import com.assemble.commons.converter.PageableConverter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableFixture {

    public static Pageable pageable_생성_기본_정렬() {
        return PageRequest.of(PAGE, SIZE, Sort.Direction.DESC, ORDER_TYPE);
    }

    public static PageableConverter pageableConverter_생성() {
        return new PageableConverter(PAGE, SIZE, ORDER_TYPE);
    }

    private static final int PAGE = 0;
    private static final int SIZE = 12;
    private static final String ORDER_TYPE = "total";
}
