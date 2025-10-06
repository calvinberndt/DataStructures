import java.util.ArrayList;

public class SwapCommand extends UndoCommand {
   // TODO: Add field declarations here
   private ArrayList<String> sourceList;
   private int index1;
   private int index2;
   private String item1;
   private String item2;

   // TODO: Add constructor code here
   public SwapCommand(ArrayList<String> list, int index1, int index2, String item1, String item2) {
      this.sourceList = list;
      this.index1 = index1;
      this.index2 = index2;
      this.item1 = item1;
      this.item2 = item2;
   }

   @Override
   public void execute() {
      sourceList.set(index1, item1);
      sourceList.set(index2, item2);
   }

}
