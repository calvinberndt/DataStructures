import java.util.ArrayList;

public class InsertAtCommand extends UndoCommand {
   // TODO: Add field declarations here
   private ArrayList<String> sourceList;
   private int insertionIndex;
   private String itemToInsert;

   // TODO: Add constructor code here
   public InsertAtCommand(ArrayList<String> list, int index, String item) {
      this.sourceList = list;
      this.itemToInsert = item;
      this.insertionIndex = index;
   }

   @Override
   public void execute() {
      sourceList.add(insertionIndex, itemToInsert);
   }
}
