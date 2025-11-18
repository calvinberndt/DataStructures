import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

// Message class representing an encrypted message
class Message {
    private int messageId;
    private String content;
    private String messageHash;
    private long timestamp;
    
    public Message(int messageId, String content) {
        this.messageId = messageId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.messageHash = generateHash(content);
    }
    
    // Generate SHA-256 hash for message integrity
    private String generateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(content.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 16); // Truncate for display
        } catch (NoSuchAlgorithmException e) {
            return "ERROR";
        }
    }
    
    public int getMessageId() { return messageId; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Message[ID=%d, Hash=%s, Content=\"%s\"]", 
                           messageId, messageHash, content);
    }
}

// Custom HashMap implementation with chaining for collision resolution
class MessageHashMap {
    private static final int DEFAULT_CAPACITY = 10;
    private static final double LOAD_FACTOR = 0.75;
    
    private LinkedList<Entry>[] table;
    private int size;
    private int collisionCount;
    
    // Entry class for key-value pairs
    private static class Entry {
        int key;
        Message value;
        
        Entry(int key, Message value) {
            this.key = key;
            this.value = value;
        }
    }
    
    @SuppressWarnings("unchecked")
    public MessageHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
        collisionCount = 0;
    }
    
    // Hash function
    private int hash(int key) {
        return Math.abs(key % table.length);
    }
    
    // Put operation - adds or updates a message
    public void put(int messageId, Message message) {
        int index = hash(messageId);
        
        // Initialize bucket if null
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        
        // Check if key already exists (update scenario)
        for (Entry entry : table[index]) {
            if (entry.key == messageId) {
                System.out.println("⚠️  Updating existing message ID: " + messageId);
                entry.value = message;
                return;
            }
        }
        
        // Collision detection
        if (!table[index].isEmpty()) {
            collisionCount++;
            System.out.println("⚠️  COLLISION detected at index " + index + 
                             " for message ID: " + messageId);
        }
        
        // Add new entry
        table[index].add(new Entry(messageId, message));
        size++;
        
        // Check load factor and resize if needed
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }
    
    // Get operation - retrieves a message by ID
    public Message get(int messageId) {
        int index = hash(messageId);
        
        if (table[index] == null) {
            return null;
        }
        
        // Search through the chain
        for (Entry entry : table[index]) {
            if (entry.key == messageId) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    // Resize the hash table when load factor is exceeded
    @SuppressWarnings("unchecked")
    private void resize() {
        System.out.println("\n🔄 Resizing hash table from " + table.length + 
                         " to " + (table.length * 2));
        LinkedList<Entry>[] oldTable = table;
        table = new LinkedList[table.length * 2];
        size = 0;
        collisionCount = 0;
        
        // Rehash all entries
        for (LinkedList<Entry> bucket : oldTable) {
            if (bucket != null) {
                for (Entry entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }
    
    // Check if a message exists
    public boolean containsKey(int messageId) {
        return get(messageId) != null;
    }
    
    // Get statistics
    public void printStats() {
        System.out.println("\n📊 HashMap Statistics:");
        System.out.println("  - Total messages: " + size);
        System.out.println("  - Table capacity: " + table.length);
        System.out.println("  - Load factor: " + String.format("%.2f", (double) size / table.length));
        System.out.println("  - Collisions detected: " + collisionCount);
    }
    
    // Display the internal structure
    public void displayTable() {
        System.out.println("\n🗂️  Hash Table Structure:");
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isEmpty()) {
                System.out.print("  Index " + i + ": ");
                for (Entry entry : table[i]) {
                    System.out.print("[ID:" + entry.key + "] ");
                }
                System.out.println("(" + table[i].size() + " entries)");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🔐 ENCRYPTED MESSAGING APP - Hash Table Demo");
        System.out.println("=".repeat(60));
        
        MessageHashMap messageStore = new MessageHashMap();
        
        // CREATE AND POPULATE: 7 sample messages
        System.out.println("\n📝 CREATING AND STORING MESSAGES:\n");
        
        Message[] messages = {
            new Message(3, "Hello, how are you?"),
            new Message(18, "Meeting at 3 PM tomorrow"),
            new Message(23, "Don't forget the presentation"),
            new Message(7, "Login successful"),
            new Message(42, "Your order has been shipped"),
            new Message(13, "Password reset requested"),
            new Message(33, "Welcome to our encrypted messaging system!")
        };
        
        // PUT operations - Add all messages
        for (Message msg : messages) {
            System.out.println("➕ Adding: " + msg);
            messageStore.put(msg.getMessageId(), msg);
        }
        
        // Display hash table structure
        messageStore.displayTable();
        messageStore.printStats();
        
        // DEMONSTRATE GET OPERATIONS
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔍 DEMONSTRATING GET OPERATIONS:\n");
        
        int[] idsToRetrieve = {18, 7, 99, 42, 13};
        for (int id : idsToRetrieve) {
            System.out.println("🔎 Retrieving message ID " + id + ":");
            Message retrieved = messageStore.get(id);
            if (retrieved != null) {
                System.out.println("   ✅ Found: " + retrieved);
                System.out.println("   🔒 Hash verification: " + retrieved.getMessageHash());
            } else {
                System.out.println("   ❌ Message not found!");
            }
            System.out.println();
        }
        
        // DEMONSTRATE PUT OPERATIONS (Updates and Collisions)
        System.out.println("=".repeat(60));
        System.out.println("✏️  DEMONSTRATING PUT OPERATIONS (Updates):\n");
        
        // Update an existing message
        System.out.println("➕ Updating message ID 18:");
        Message updatedMsg = new Message(18, "Meeting CANCELLED - postponed to next week");
        messageStore.put(18, updatedMsg);
        System.out.println("   ✅ Updated to: " + messageStore.get(18));
        System.out.println();
        
        // Add new messages that might cause collisions
        System.out.println("➕ Adding message ID 28 (may collide with ID 18):");
        Message newMsg1 = new Message(28, "Testing collision handling");
        messageStore.put(28, newMsg1);
        System.out.println();
        
        System.out.println("➕ Adding message ID 8 (may collide with ID 18 & 28):");
        Message newMsg2 = new Message(8, "Another potential collision");
        messageStore.put(8, newMsg2);
        
        // Final display
        messageStore.displayTable();
        messageStore.printStats();
        
        // VERIFY MESSAGE INTEGRITY
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 MESSAGE INTEGRITY VERIFICATION:\n");
        
        Message msg = messageStore.get(42);
        if (msg != null) {
            System.out.println("Original message: " + msg);
            System.out.println("Stored hash: " + msg.getMessageHash());
            
            // Simulate hash verification
            Message testMsg = new Message(42, msg.getContent());
            System.out.println("Recomputed hash: " + testMsg.getMessageHash());
            
            if (msg.getMessageHash().equals(testMsg.getMessageHash())) {
                System.out.println("✅ Message integrity verified - hashes match!");
            } else {
                System.out.println("❌ WARNING: Message may have been tampered with!");
            }
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ Demo completed!");
        System.out.println("=".repeat(60));
    }
}