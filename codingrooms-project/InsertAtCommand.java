import java.util.ArrayList;

public class InsertAtCommand extends UndoCommand {
   // TODO: Add field declarations here
   private ArrayList<String> sourceList;
   private int insertionIndex;

   // TODO: Add constructor code here
   public InsertAtCommand(ArrayList<String> list, int index) {
      this.sourceList = list;
      this.insertionIndex = index;
   }

   @Override
   public void execute() {
      sourceList.add(insertionIndex, sourceList.remove(insertionIndex));
   }
}
