package com.nowcoder.service;

import com.nowcoder.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SeacherService {
    private static final String SOLR_URL = "http://localhost:80/solr/collection";
//http://localhost/solr/#/collection1/query
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    public List<Question> searchQuestion(String keyword, int offset, int count, String hlPre, String hlPos) throws IOException, SolrServerException {
        List<Question> questionList = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery(keyword);
        solrQuery.setStart(offset);
        solrQuery.setRows(count);
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre(hlPre);
        solrQuery.setHighlightSimplePost(hlPos);
        solrQuery.set("hl.fl", QUESTION_CONTENT_FIELD + "," + QUESTION_TITLE_FIELD);

        QueryResponse queryResponse = client.query(solrQuery);
        for(Map.Entry<String,Map<String,List<String>>> entry:queryResponse.getHighlighting().entrySet()){
            Question question=new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contentList=entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(contentList.size()>0){
                    question.setContent(contentList.get(0));
                }
            }

            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titleList=entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titleList.size()>0){
                    question.setTitle(titleList.get(0));
                }
            }
            questionList.add(question);
        }
        return questionList;
    }


    public boolean indexQuestion(Integer qid,String title,String content) throws IOException, SolrServerException {
        SolrInputDocument doc=new SolrInputDocument();
        doc.setField("id",qid);
        doc.setField(QUESTION_TITLE_FIELD,title);
        doc.setField(QUESTION_CONTENT_FIELD,content);
        UpdateResponse response=client.add(doc,1000);
        return response!=null&&response.getStatus()==0;
    }
}
