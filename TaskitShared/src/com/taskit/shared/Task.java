/**
 * Task.java
 * 2012-9-25 Qixuan Zhang Created
 */

package com.taskit.shared;

import com.google.api.client.util.DateTime;

public class Task {
	
	// Status of the task. This is either "needsAction" or "completed".
	private String status;
	
	// Last modification time of the task (as a RFC 3339 timestamp).
	private DateTime updated;
	
	// Parent task identifier. This field is omitted if it is a top-level task. This field is read-
	// only. Use the "move" method to move the task under a different parent or to the top level.
	private String parent;

	// Title of the task.
	private String title;

	// Flag indicating whether the task has been deleted. The default if False.
	private Boolean deleted;
	
	// Completion date of the task (as a RFC 3339 timestamp). This field is omitted if the task has
	// not been completed.
	private DateTime completed;
	
	// Due date of the task (as a RFC 3339 timestamp). Optional.
	private DateTime due;

	// ETag of the resource.
	private String etag;

	// notes describing the task. Optional.
	private String notes;

	// String indicating the position of the task among its sibling tasks under the same parent task
   	// at the top level. If this string is greater than another task's corresponding position
	// string according to lexicographical ordering, the task is positioned after the other task under
	// the same parent task (or at the top level). This field is read-only. Use the "move" method to
	// move the task to another position.
	private String position;

	// Flag indicating whether the task is hidden. This is the case if the task had been marked
	// completed when the task list was last cleared. The default is False. This field is read-only.
	private Boolean hidden;

	// Task identifier.
	private String id;

	// URL pointing to this task. Used to retrieve, update, or delete this task.
	private String selfLink;

	public String getStatus() {
		return status;
	}

	public Task setStatus(String status) {
	    this.status = status;
	    return this;
	}

	public DateTime getUpdated() {
	    return updated;
	}

	public Task setUpdated(DateTime updated) {
	    this.updated = updated;
	    return this;
	}

	public String getParent() {
	    return parent;
	}

	public Task setParent(String parent) {
	    this.parent = parent;
	    return this;
	}
	
	public String getTitle() {
	    return title;
	}

	public Task setTitle(String title) {
	    this.title = title;
	    return this;
	}

	public Boolean getDeleted() {
	    return deleted;
	}
	
	public Task setDeleted(Boolean deleted) {
	    this.deleted = deleted;
	    return this;
	}

	public DateTime getCompleted() {
	    return completed;
	}

	public Task setCompleted(DateTime completed) {
	    this.completed = completed;
	    return this;
	}

	public DateTime getDue() {
	    return due;
	}

	public Task setDue(DateTime due) {
	    this.due = due;
	    return this;
	}

	public String getEtag() {
	    return etag;
	}

	public Task setEtag(String etag) {
	    this.etag = etag;
	    return this;
	}

	public String getNotes() {
	    return notes;
	}

	public Task setNotes(String notes) {
	    this.notes = notes;
	    return this;
	}

	public String getPosition() {
	    return position;
	}

	public Task setPosition(String position) {
	    this.position = position;
	    return this;
	}

	public Boolean getHidden() {
	    return hidden;
	}

	public Task setHidden(Boolean hidden) {
	    this.hidden = hidden;
	    return this;
	}

	public String getId() {
	    return id;
	}

	public Task setId(String id) {
	    this.id = id;
	    return this;
	}

	public String getSelfLink() {
	    return selfLink;
	}
	
	public Task setSelfLink(String selfLink) {
	    this.selfLink = selfLink;
	    return this;
	}
}
