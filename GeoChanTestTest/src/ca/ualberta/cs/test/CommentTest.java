package ca.ualberta.cs.test;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.models.CommentModel;
import ca.ualberta.cs.models.TopicModel;
import ca.ualberta.cs.models.UserModel;


public class CommentTest extends ActivityInstrumentationTestCase2<Activity> {
	public CommentTest(){
		super(Activity.class);
	}
	/**
	 * TestCase 5.1
	 * Test whether a topic's comments are displayed
	 */
	public void testCommentDisplay() {
		TopicModel testTopic = new TopicModel();
		CommentModel testComment = new CommentModel();
		testTopic.addChildComment(testComment);
		
	}
	
	/**
	 * TestCase 6.2
	 * Test whether a new comment can be created
	 */
	public void testCommentCreation() {
		CommentModel testComment = new CommentModel();
		assertNotNull("Comment is not created", testComment);
	}
	
	/**
	 * TestCase 6.3
	 * Test whether a new comment is correctly attached to its parent
	 */
	public void testCommentAssociation() {
		TopicModel testTopic = new TopicModel();
		CommentModel testComment = new CommentModel();
		testTopic.addChildComment(testComment);
		assertEquals("Comment not properly associated with Topic", testComment.getMyFirstAncestor(), testTopic);
	}

	/**
	 * TestCase 21.1
	 * Test whether comment takes user and allows for username retreival
	 */
	public void testCommentUser() {
		String theUserName = "Testuser";
		UserModel user = new UserModel(theUserName);
		
		CommentModel comment = new CommentModel();
		comment.setPostedBy(user);
		
		assertEquals(theUserName, comment.getPostedBy().getUserName());
	}
}
