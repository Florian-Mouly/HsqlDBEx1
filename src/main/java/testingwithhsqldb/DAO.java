package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfCustomer(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT LastName FROM Customer WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("LastName");
				}
			}
		}
		// dernière ligne : on renvoie le résultat
		return result;
	}
        
        public void insertProduct(ProductEntity produit) throws SQLException{
            String sql = "INSERT INTO Product VALUE(?,?,?)";
            
            try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
            ) {
			stmt.setInt(1,produit.getId());
                        stmt.setString(2, produit.getNom());
                        stmt.setFloat(3, produit.getPrix());
                        
                        stmt.executeUpdate();
            
            }
        }
        
        public ProductEntity findProduct(int id) throws SQLException{
            String sql = "SELECT * FROM Product WHERE ID = ?";
            try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
            ) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
		    ProductEntity produit = new ProductEntity(rs.getInt("ID"),rs.getString("Name"),rs.getInt("Price"));
                    return produit;
		}
            }
        }
	
}
