package com.sanri.test.sqlparser.items;

import com.sanri.test.sqlparser.ParserItem;
import net.sf.jsqlparser.statement.select.SubJoin;

public class SqlSubJoin implements ParserItem {
    private SubJoin subJoin;

    public SqlSubJoin(SubJoin subJoin) {
        this.subJoin = subJoin;
    }

    public void parser() {

    }
}
