package ca.ualberta.cs.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.CommentModel;
import ca.ualberta.cs.MainActivity;
import ca.ualberta.cs.PostListController;
import ca.ualberta.cs.PostModel;
import ca.ualberta.cs.TopicModel;

public class PostListControllerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public PostListControllerTest() {
		super(MainActivity.class);
	}
	
	public void testControllerGetList(){
		ArrayList<Object> commentList = PostListController.getList();
		assertNotNull(commentList);
	}
	
	public void testControllerCreateTopicList(){
		ArrayList<TopicModel> model = PostListController.createTopicList();
		assertNotSame("make sure objects are not all the same", model.get(0), model.get(1));
	}
}