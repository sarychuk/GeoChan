package ca.ualberta.cs.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.controllers.PostListController;
import ca.ualberta.cs.models.CommentModel;
import ca.ualberta.cs.models.TopicModel;
import ca.ualberta.cs.models.UserModel;
import ca.ualberta.cs.views.MainActivity;
import ca.ualberta.cs.views.PostListViewAdapter;

public class PostApaterTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	public PostApaterTest() {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	public void testPostApaterExists() {
		// build comment object list
		UserModel user = new UserModel("Wyatt");
		CommentModel comment = new CommentModel(user);
		ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
		commentList.add(comment);
		PostListViewAdapter adapter = new PostListViewAdapter(getActivity(),
				commentList);

		// make sure the adapter isnt null
		assertNotNull("The objcet is not null", adapter);
	}

	public void TestAdapterCount() {
		// build mutli comment item
		ArrayList<CommentModel> commentList = PostListController
				.createCommentlist(new UserModel("testUser"));
		PostListViewAdapter adapter = new PostListViewAdapter(getActivity(),
				commentList);
		// check to see if it can get the right number of items
		assertEquals("Check the number of items in adapter", 3,
				adapter.getCount());
	}

	public void TestAdapterItem() {
		// build mutli comment item
		ArrayList<CommentModel> commentList = PostListController
				.createCommentlist(new UserModel("testUser"));
		PostListViewAdapter adapter = new PostListViewAdapter(getActivity(),
				commentList);

		// make sure we are still getting the correct object
		assertEquals(CommentModel.class, adapter.getItem(1).getClass());
	}
	
	public void testBuggyJunk(){
		//TestAdapterCount();
		//TestAdapterItem();
	}

	public void testCommentedTopics() {

		ArrayList<TopicModel> topicList = (ArrayList<TopicModel>) PostListController
				.createCommentedTopics(new UserModel("Testuser"));
		assertNotNull("Comments exist", topicList.get(0).getChildrenComments());
		assertEquals("Comments are comments", CommentModel.class, topicList.get(0).getChildrenComments().get(0).getClass());
		assertEquals("The number of coments exists", 3, topicList.get(0).getChildrenComments().size());
		}

}