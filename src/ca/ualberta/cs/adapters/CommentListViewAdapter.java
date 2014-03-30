package ca.ualberta.cs.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.R;
import ca.ualberta.cs.models.CommentModel;

public class CommentListViewAdapter extends PostListViewAdapter<CommentModel> {
	private final static int excerptLength = 50;

	public CommentListViewAdapter(Activity activity,
			ArrayList<CommentModel> arrayList) {
		super(activity, arrayList);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateCellTitle(View theView, CommentModel thePost) {
		// List of comments, display an excerpt of their comment
		
		String commentText = thePost.getCommentText();
		
		if (commentText != null) {
			TextView titleText = (TextView) theView.findViewById(R.id.textViewTitle);
			titleText.setText(trimString(commentText, excerptLength));
		}
	}
}
