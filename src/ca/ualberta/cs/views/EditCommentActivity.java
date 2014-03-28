package ca.ualberta.cs.views;

import java.util.Currency;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.R;
import ca.ualberta.cs.controllers.CommentModelController;
import ca.ualberta.cs.models.ActiveUserModel;
import ca.ualberta.cs.models.CommentModel;
import ca.ualberta.cs.models.CommentModelList;
import ca.ualberta.cs.models.CurrentUserPostModelFactory;
import ca.ualberta.cs.models.TopicModelList;

public class EditCommentActivity extends EditPostActivity<CommentModel> {

	public static final String IS_NEW_COMMENT_KEY = "IS_NEW_COMMENT";

	private CommentModelController theController;
	private Boolean isNewComment = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// Get the extras
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			this.isNewComment = extras.getBoolean(IS_NEW_COMMENT_KEY);
		}

		if (this.isNewComment) {
			theModel = new CommentModel(ActiveUserModel.getInstance().getUser());
		}

		// Get the controller
		this.theController = new CommentModelController(
				TopicModelList.getInstance());

		// customize UI
		EditText commentEditTitle = (EditText) findViewById(R.id.titleTextField);
		commentEditTitle.setVisibility(View.INVISIBLE);

		if (CommentModelList.getInstance().getSelection() != null){
			System.out.println(CommentModelList.getInstance().getSelection().getCommentText());
		}
		else {
			System.out.println(TopicModelList.getInstance().getSelection().getCommentText());
		}

		// Populate the view
		populateView();
	}

	@Override
	protected void populateView() {
		Button saveButton = (Button) findViewById(R.id.saveOrAddButton);

		if (this.isNewComment) {
			saveButton.setText("Add Comment");
			saveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Get the comment
					EditText commentField = (EditText) findViewById(R.id.commentTextField);
					theModel.setCommentText(commentField.getText().toString());

					// add the picture
					theModel.setPicture(imageBitmap);

					// add to the selected model and then reset the selected
					// model so as to reply to the correct topic
					if (CommentModelList.getInstance().getSelection() == null) {
						theController.addComment(theModel, TopicModelList
								.getInstance().getSelection());
					} else {
						theController.addComment(theModel, CommentModelList
								.getInstance().getSelection());
						CommentModelList.getInstance().resetSelection();
					}
					finish();
				}
			});

		} else {
			saveButton.setText("Update Comment");

		}

		// get photo button
		Button cameraButton = (Button) findViewById(R.id.pictureButton);

		// set onclick listener
		cameraButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// start camera activity
				getPictureIntent();
			}
		});

	}

	public Boolean getIsNewTopic() {
		return isNewComment;
	}
}
