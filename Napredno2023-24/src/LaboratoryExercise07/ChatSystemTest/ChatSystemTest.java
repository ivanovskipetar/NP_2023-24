package LaboratoryExercise07.ChatSystemTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super(String.format("%s does not exist", roomName));
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String userName) {
        super(String.format("%s does not exist", userName));
    }
}

class ChatRoom implements Comparable<ChatRoom> {
    private String roomName;
    private TreeSet<String> users;

    public ChatRoom(String name) {
        this.roomName = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        if (hasUser(username))
            users.remove(username);
    }

    public boolean hasUser(String username) {
        return this.users.contains(username);
    }

    public int numUsers() {
        return this.users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomName).append("\n");

        if (users.isEmpty())
            sb.append("EMPTY");

        users.forEach(s -> sb.append(s).append("\n"));

        return sb.toString();
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public int compareTo(ChatRoom o) {
        return Integer.compare(this.numUsers(), o.numUsers());
    }
}

class ChatSystem {
    private TreeMap<String, ChatRoom> rooms;
    private Set<String> registeredUsers;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.registeredUsers = new HashSet<>();
    }

    public void addRoom(String roomName) {
        rooms.putIfAbsent(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        if (rooms.get(roomName) != null)
            rooms.remove(roomName);
    }

    public void register(String userName) {
        registeredUsers.add(userName);
        rooms.values().stream()
                .min(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getRoomName))
                .ifPresent(room -> room.addUser(userName));
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException {
        registeredUsers.add(userName);
        getRoom(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!registeredUsers.contains(userName)) throw new NoSuchUserException(userName);

        getRoom(roomName).addUser(userName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!registeredUsers.contains(userName)) throw new NoSuchUserException(userName);

        getRoom(roomName).removeUser(userName);
    }

    public void followFriend(String userName, String friend_username) throws NoSuchUserException {
        if (!registeredUsers.contains(userName)) throw new NoSuchUserException(userName);
        if (!registeredUsers.contains(friend_username)) throw new NoSuchUserException(friend_username);

        rooms.values()
                .stream()
                .filter(chatRoom -> chatRoom.hasUser(friend_username))
                .forEach(chatRoom -> chatRoom.addUser(userName));
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (rooms.get(roomName) == null) throw new NoSuchRoomException(roomName);
        return rooms.get(roomName);
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}
