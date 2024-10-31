package be.th.ski_school_manager;

public class SkiSchoolManagerProgram {
	public static void main(String[] args) {
		DAO<Secretary> secretaryDAO = new SecretaryDAO(SkiSchoolConnection.getInstance());
		Secretary secretary = secretaryDAO.find(21);
		System.out.println(secretary);
	}
}
