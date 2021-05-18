package main.java.com.excilys.cdb.ui;

public enum MenuChoice {
	MENU(0),COMPUTER_LIST(1),COMPANY_LIST(2),CHOSE_COMPUTER(3),ADD_COMPUTER(4),
	UPDATE_COMPUTER(5),DELETE_COMPUTER(6),EXIT(7);
	
	private int number;
	
	MenuChoice(int number){
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

}
