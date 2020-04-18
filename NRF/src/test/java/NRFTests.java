import Database.DatabaseConnection;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class NRFTests {
    @Test
    public  void databaseConnection(){
         DatabaseConnection db = new DatabaseConnection();
         assertNotNull(db.getStmt());
    }

}
