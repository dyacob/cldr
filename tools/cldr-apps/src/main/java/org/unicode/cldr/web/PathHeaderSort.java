package org.unicode.cldr.web;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.unicode.cldr.util.PathHeader;
import org.unicode.cldr.web.DataPage.DataRow;

public class PathHeaderSort extends SortMode {

    public static String name = "ph";

    @Override
    String getName() {
        return name;
    }

    @Override
    String getDisplayName() {
        return getName(); // dont care
    }

    @Override
    Comparator<DataRow> getComparator() {
        return null; // not used
    }

    @Override
    public DataPage.DisplaySet createDisplaySet(XPathMatcher matcher, Collection<DataRow> values) {
        DataRow[] rows = createSortedList(new Comparator<>() {

            @Override
            public int compare(DataRow ll, DataRow rr) {
                PathHeader l = ll.getPathHeader();
                PathHeader r = rr.getPathHeader();
                return l.compareTo(r);
            }
        }, matcher, values);

        List<Partition> thePartitions = new LinkedList<>();
        Partition last = null;
        String headerLast = null;
        for (int i = 0; i < rows.length; i++) {
            String headerNext = rows[i].getPathHeader().getHeader();
            if (headerNext == headerLast || (headerNext.equals(headerLast))) {
                last.limit = i + 1;
            } else {
                last = new Partition((headerLast = headerNext), i, i + 1);
                if ("null".equals(headerLast)) {
                    last.name = "";
                }
                thePartitions.add(last);
            }
        }

        return new DataPage.DisplaySet(rows, this, thePartitions.toArray(new Partition[0]));
    }

}
