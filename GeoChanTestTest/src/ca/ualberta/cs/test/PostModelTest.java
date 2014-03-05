package ca.ualberta.cs.test;

import ca.ualberta.cs.views.MainActivity;
import ca.ualberta.cs.models.PostModel;
import ca.ualberta.cs.models.UserModel;
import android.test.ActivityInstrumentationTestCase2;

public class PostModelTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public PostModelTest() {
		super(MainActivity.class);
	}

	public void testPostModel() {
		UserModel user = new UserModel("Wyatt");
		PostModel postModel = new PostModel(user);

		assertNotNull("Built post model isn't null", postModel);
		assertEquals("Post user is assigned", user, postModel.getPostedBy());
		
		postModel.incrementScore();
		assertEquals("Increment score test", postModel.getScore(), Integer.getInteger("1"));
		
		postModel.decrementScore();
		assertEquals("Descrement score test", postModel.getScore(), Integer.getInteger("0"));
	}
}
