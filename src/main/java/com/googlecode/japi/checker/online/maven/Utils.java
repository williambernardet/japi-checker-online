package com.googlecode.japi.checker.online.maven;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.maven.index.Field;
import org.apache.maven.index.Indexer;
import org.apache.maven.index.MAVEN;
import org.apache.maven.index.SearchType;
import org.apache.maven.index.expr.SearchTypedStringSearchExpression;
import org.apache.maven.index.expr.SourcedSearchExpression;


final class Utils {
    private static final Pattern QUERY_PATTERN = Pattern.compile("^([gav]):(\\S+)$");
    private Utils() {
    }
    public static Query toQuery(Indexer indexer, String query) {
    	return toQuery(indexer, query, true);
    }    
    public static Query toQuery(Indexer indexer, String query, boolean exactMatch) {
        BooleanQuery q = new BooleanQuery();
        SearchType searchType = exactMatch? SearchType.EXACT : SearchType.SCORED;
        q.add(indexer.constructQuery(MAVEN.PACKAGING, new SourcedSearchExpression("jar")), Occur.MUST);
        q.add(indexer.constructQuery(MAVEN.CLASSIFIER, new SourcedSearchExpression(Field.NOT_PRESENT)), Occur.MUST_NOT);
        if (!query.contains(":")) {
            q.add(indexer.constructQuery(MAVEN.ARTIFACT_ID, new SearchTypedStringSearchExpression(query, searchType)), Occur.MUST);
        } else {
            for (String item : query.split("\\s+")) {
                Matcher m = QUERY_PATTERN.matcher(item);
                if (m.matches()) {
                    switch (m.group(1).charAt(0)) {
                        case 'g':
                            q.add(indexer.constructQuery(MAVEN.GROUP_ID, new SearchTypedStringSearchExpression(m.group(2), searchType)), Occur.MUST);
                            break;
                        case 'a':
                            q.add(indexer.constructQuery(MAVEN.ARTIFACT_ID, new SearchTypedStringSearchExpression(m.group(2), searchType)), Occur.MUST);
                            break;
                        case 'v':
                            q.add(indexer.constructQuery(MAVEN.VERSION, new SearchTypedStringSearchExpression(m.group(2), searchType)), Occur.MUST);
                            break;
                    }
                } else {
                    // fail...
                }
            }
        }
        return q;
    }
    
}