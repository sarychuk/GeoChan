/**
 * 
 */
package ca.ualberta.cs.models;

import java.util.ArrayList;

import android.location.Location;

/**
 * @author wyatt
 * 
 */
public class UserModel {
	private String userName;
	private Location location;
	private ArrayList<String> upVoteList;
	private ArrayList<String> downVoteList;

	public UserModel(String theUserName) {
		userName = theUserName;
		upVoteList = new ArrayList<String>();
		downVoteList = new ArrayList<String>();
	}

	public ArrayList<String> getUpVoteList() {
		return upVoteList;
	}

	public void addPostIdUpVoteList(String id) {
		upVoteList.add(id);
	}

	public void removePostIdUpVoteList(String id) {
		upVoteList.remove(id);
	}

	public ArrayList<String> getDownVoteList() {
		return downVoteList;
	}

	public void addPostIdDownVoteList(String id) {
		downVoteList.add(id);
	}

	public void removePostIdDownVoteList(String id) {
		downVoteList.remove(id);
	}

	public String getUserName() {
		return userName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
