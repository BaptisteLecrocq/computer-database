package com.excilys.cdb.model;

public class User {
	
	private int id;
	private String username;
	private String password = "";
	private String role = "user";
	private boolean enabled;
	
	private User(UserBuilder builder) {
		
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;
		this.role = builder.role;
		
	}
	
	public static class UserBuilder {
		
		private int id;
		private String username;
		private String password;
		private String role;
		private boolean enabled;
		
		public UserBuilder(String username) {
			this.username = username;
		}
		
		public UserBuilder withId(int id) {
			this.id = id;
			return(this);
		}
		
		public UserBuilder withusername(String username) {
			this.username = username;
			return(this);
		}
		
		public UserBuilder withPassword(String password) {
			this.password = password;
			return(this);
		}
		
		public UserBuilder withRole(String role) {
			this.role = role;
			return(this);
		}
		
		public UserBuilder withEnabled(int enabled) {
			
			if(enabled == 1) {
				this.enabled = true;
			}
			else {
				this.enabled = false;
			}
			return(this);
			
		}
		
		public User build() {			
			User user = new User(this);
			return(user);			
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	

}
