/**
 * 
 */
package ca.ualberta.cs.models;

/**
 * @author wyatt
 *
 */
public class TopicModelList extends PostModelList<TopicModel> implements UpdateableListInterface {
	private static TopicModelList singleton = null;
	
	/*
	 * Returns a shared TopicModelList
	 */
	public static TopicModelList getInstance() {
		if (singleton == null) {
			singleton = new TopicModelList();
		}
		
		return singleton;
	}
	
	/*
	 * Sorts theTopicModelArrayList by location
	 */
	public void sortByLocation() {
		// TODO: Implement method
	}

	@Override
	public void updateFromNetwork() {
		// TODO Auto-generated method stub
		
	}
}
