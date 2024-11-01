package be.th.ski_school_manager;

public class SkiSchoolManagerProgram {
	public static void main(String[] args) {
		DAOFactory daoFactory = new DAOFactory();
		
		DAO<Secretary> secretaryDAO = daoFactory.getSecretaryDAO();
		
		Secretary secretary = secretaryDAO.find(22);
		
		System.out.println(secretary);
	}
}
