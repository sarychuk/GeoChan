package ca.ualberta.cs.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.cs.models.CommentModel;
import ca.ualberta.cs.models.ElasticSearchMgetResponse;
import ca.ualberta.cs.models.ElasticSearchOperationFactory;
import ca.ualberta.cs.models.ElasticSearchOperationRequest;
import ca.ualberta.cs.models.ElasticSearchOperationResponse;
import ca.ualberta.cs.models.ElasticSearchResponse;
import ca.ualberta.cs.models.ElasticSearchSearchResponse;
import ca.ualberta.cs.models.FollowingPostModelList;
import ca.ualberta.cs.models.TopicModel;
import ca.ualberta.cs.models.UpdatePackage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * Manages the ES requests
 * 
 * @author wyatt
 */
public enum ElasticSearchProviderServiceHandler {
	/**
	 * Adds a topic to the database
	 */
	ADD_TOPIC {

		@Override
		public ElasticSearchOperationResponse doInBackground(
				ElasticSearchOperationRequest theRequest) {
			TopicModel theTopic = theRequest.getTopicModel();

			HttpPost request = new HttpPost(getEndpointUrl("topic"));

			String jsonString = gson.toJson(theTopic);

			// Add to entity
			try {
				request.setEntity(new StringEntity(jsonString));
				// run it
				HttpResponse response = client.execute(request);

				String jsonResponse = getStringFromResponse(response);

				Log.w("ElasticSearchProviderService", jsonResponse);

				// We have to tell GSON what type we expect
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<TopicModel>>() {
				}.getType();

				ElasticSearchResponse<TopicModel> esResponse = gson.fromJson(
						jsonResponse, elasticSearchResponseType);

				theTopic.setId(esResponse.getId());

				ElasticSearchOperationResponse theResponse = ElasticSearchOperationFactory
						.responseFromRequest(theRequest, esResponse);

				return theResponse;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void onPostExecute(ElasticSearchOperationResponse theResponse) {
			theResponse.getPostModelList().add(theResponse.getTopicModel());

			// Required as to add the elasticsearch id into the db as "id"
			// rather than "_id"
			ElasticSearchProvider.getProvider()
					.updateTopic(theResponse.getTopicModel(),
							theResponse.getPostModelList());

		}

	},
	/**
	 * Updates a topic in the database
	 */
	UPDATE_TOPIC {

		@Override
		public ElasticSearchOperationResponse doInBackground(
				ElasticSearchOperationRequest theRequest) {
			TopicModel theTopic = theRequest.getTopicModel();

			HttpPost request = new HttpPost(getVersionedEndpoint("topic",
					theTopic));

			String jsonString = gson.toJson(theTopic);

			// Add to entity
			try {
				request.setEntity(new StringEntity(jsonString));
				// run it
				HttpResponse response = client.execute(request);

				String jsonResponse = getStringFromResponse(response);

				Log.w("ElasticSearchProviderService", jsonResponse);

				int responseCode = response.getStatusLine().getStatusCode();

				// TODO: Handle a 409 error (When a version mismatch occurs)

				// We have to tell GSON what type we expect
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<TopicModel>>() {
				}.getType();

				ElasticSearchResponse<TopicModel> esResponse = gson.fromJson(
						jsonResponse, elasticSearchResponseType);

				ElasticSearchOperationResponse theResponse = ElasticSearchOperationFactory
						.responseFromRequest(theRequest, esResponse);

				return theResponse;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void onPostExecute(ElasticSearchOperationResponse theResponse) {
			theResponse.getPostModelList().update(theResponse.getTopicModel());
		}

	},
	/**
	 * Fetches posts according to a sorting routine
	 */
	GET_POSTS {

		@Override
		public ElasticSearchOperationResponse doInBackground(
				ElasticSearchOperationRequest theRequest) {
			HttpPost request = new HttpPost(getEndpointUrl("topic/_search"));
			String query = theRequest.getQueryString();

			try {
				StringEntity stringentity = new StringEntity(query);
				request.setHeader("Accept", "application/json");
				request.setEntity(stringentity);

				HttpResponse response = client.execute(request);

				String jsonResponse = getStringFromResponse(response);

				// We have to tell GSON what type we expect
				Type elasticSearchResponseType = new TypeToken<ElasticSearchSearchResponse<TopicModel>>() {
				}.getType();

				ElasticSearchSearchResponse<TopicModel> esResponse = gson
						.fromJson(jsonResponse, elasticSearchResponseType);

				ElasticSearchOperationResponse theResponse = ElasticSearchOperationFactory
						.responseFromRequest(theRequest, esResponse);

				return theResponse;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void onPostExecute(ElasticSearchOperationResponse theResponse) {
			theResponse.getPostModelList().setArrayList(
					theResponse.getTheTopicModels());
		}

	},
	/**
	 * Multi get posts
	 */
	MULTI_GET_TOPICS {

		@Override
		public ElasticSearchOperationResponse doInBackground(
				ElasticSearchOperationRequest theRequest) {
			HttpPost request = new HttpPost(getEndpointUrl("topic/_mget"));
			String query = theRequest.generateMultiGetQueryStringForTopics();

			try {
				StringEntity stringentity = new StringEntity(query);
				request.setHeader("Accept", "application/json");
				request.setEntity(stringentity);

				HttpResponse response = client.execute(request);

				String jsonResponse = getStringFromResponse(response);

				// We have to tell GSON what type we expect
				Type elasticSearchResponseType = new TypeToken<ElasticSearchMgetResponse<TopicModel>>() {
				}.getType();

				ElasticSearchMgetResponse<TopicModel> esResponse = gson
						.fromJson(jsonResponse, elasticSearchResponseType);

				ElasticSearchOperationResponse theResponse = ElasticSearchOperationFactory
						.responseFromTopicsRequest(theRequest, esResponse);

				return theResponse;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void onPostExecute(ElasticSearchOperationResponse theResponse) {
			FollowingPostModelList<TopicModel> thePostList = theResponse
					.getTheFollowingTopicsList();
			ArrayList<UpdatePackage<TopicModel>> thePackages = theResponse
					.getTheTopicIdsToGet();

			thePostList.setUpdatedPackages(thePackages);
		}

	},
	/**
	 * Multiget comments
	 */
	MULTI_GET_COMMENTS {

		@Override
		public ElasticSearchOperationResponse doInBackground(
				ElasticSearchOperationRequest theRequest) {
			HttpPost request = new HttpPost(getEndpointUrl("topic/_mget"));
			String query = theRequest.generateMultiGetQueryStringForComments();

			try {
				StringEntity stringentity = new StringEntity(query);
				request.setHeader("Accept", "application/json");
				request.setEntity(stringentity);

				HttpResponse response = client.execute(request);

				String jsonResponse = getStringFromResponse(response);

				// We have to tell GSON what type we expect
				Type elasticSearchResponseType = new TypeToken<ElasticSearchMgetResponse<TopicModel>>() {
				}.getType();

				ElasticSearchMgetResponse<TopicModel> esResponse = gson
						.fromJson(jsonResponse, elasticSearchResponseType);

				ElasticSearchOperationResponse theResponse = ElasticSearchOperationFactory
						.responseFromCommentsRequest(theRequest, esResponse);

				return theResponse;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public void onPostExecute(ElasticSearchOperationResponse theResponse) {
			FollowingPostModelList<CommentModel> thePostList = theResponse
					.getTheFollowingCommentsList();
			ArrayList<UpdatePackage<CommentModel>> thePackages = theResponse
					.getTheCommentIdsToGet();

			thePostList.setUpdatedPackages(thePackages);
		}

	};

	/**
	 * Performs the request on a background thread
	 * 
	 * @param theRequest
	 * @return
	 */
	public abstract ElasticSearchOperationResponse doInBackground(
			ElasticSearchOperationRequest theRequest);

	/**
	 * Executed on the main thread, updates UI
	 * 
	 * @param theResponse
	 */
	public abstract void onPostExecute(
			ElasticSearchOperationResponse theResponse);

	/**
	 * The url for the db
	 */
	private static String urlIndex = "http://cmput301.softwareprocess.es:8080/cmput301w14t12/";

	/**
	 * The gson serilization object
	 */
	private static Gson gson = GeoChanGsonNetworked.getGson();

	/**
	 * The HTTP client
	 */
	private static HttpClient client = new DefaultHttpClient();

	/**
	 * Fetches a string from a response object in a smarter fashion
	 */
	private static String getStringFromResponse(HttpResponse response) {
		String json = "";
		try {
			// get the entity
			response.getStatusLine().toString();
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			json = reader.readLine();

			String output;

			while ((output = reader.readLine()) != null) {
				json += output;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Utility method to get an endpoint url
	 * 
	 * @param endpoint
	 * @return
	 */
	private static String getEndpointUrl(String endpoint) {
		return urlIndex + "/" + endpoint;
	}

	/**
	 * Utility method for getting a versioned url at an endpoint
	 * 
	 * @param endpoint
	 * @param theTopic
	 * @return
	 */
	private static String getVersionedEndpoint(String endpoint,
			TopicModel theTopic) {
		String theUrl = urlIndex + "/" + endpoint + "/";

		String theVersion = Integer.toString(theTopic.getVersion());

		return theUrl + theTopic.getId() + "?version=" + theVersion;
	}
}
