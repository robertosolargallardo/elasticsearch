package com.tbd.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class BookDao {
	private final String INDEX = "bookdata";
	private final String TYPE = "books";
	private RestHighLevelClient restHighLevelClient;
	private ObjectMapper objectMapper;

	public BookDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	public Book insertBook(Book book) {
		book.setId(UUID.randomUUID().toString());
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, book.getId()).source(objectMapper.convertValue(book, Map.class));
		try {
			restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
		return book;
	}

	public Map<String, Object> getBookById(String id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		return sourceAsMap;
	}

	public Map<String, Object> updateBookById(String id, Book book) {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id).fetchSource(true);
		Map<String, Object> error = new HashMap<>();
		error.put("Error", "Unable to update book");
		try {
			String bookJson = objectMapper.writeValueAsString(book);
			updateRequest.doc(bookJson, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
			Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
			return sourceAsMap;
		} catch (JsonProcessingException e) {
			e.getMessage();
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		return error;
	}

	public void deleteBookById(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		try {
			restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
	}

}
