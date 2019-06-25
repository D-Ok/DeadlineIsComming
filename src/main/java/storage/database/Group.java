package storage.database;

public class Group {

	private String name;
	private String description;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Group(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Group(int id, String name, String description) {
		this.id=id;
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", description=" + description + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

