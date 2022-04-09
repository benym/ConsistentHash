import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Test {

    // 节点Ip前缀
    private static final String IP_PREFIX = "192.168.1.";

    public static void main(String[] args) {
        // 每台真实机器节点上保存的记录条数
        Map<String, Integer> map = new HashMap<>();

        // 真实机器节点
        List<Node<String>> nodes = new ArrayList<>();
        // 10台真实机器节点集群
        for (int i = 1; i <= 10; i++) {
            // 每台真实机器节点上保存的记录条数初始为0
            map.put(IP_PREFIX + i, 0);
            Node<String> node = new Node<>(IP_PREFIX + i, "node" + i);
            nodes.add(node);
        }
        // hash函数实例
        HashFunction hashFunction = new HashFunctionImpl();

        // 每台真实机器引入100个虚拟节点
        ConsistentHash<Node<String>> consistentHash = new ConsistentHash<>(hashFunction, 100,
                nodes);

        // 将5000条记录尽可能均匀的存储到10台机器节点
        for (int i = 0; i < 500; i++) {
            // 产生随机一个字符串当做一条记录，可以是其他更复杂的业务对象
            // 这里随机字符串相当于对象的业务唯一标识
            String data = UUID.randomUUID().toString() + i;
            // 通过记录找到真实机器节点
            Node<String> node = consistentHash.get(data);
            // 在这里可以通过其他工具将记录存储到真实机器节点上，比如Redis
            // ...
            // 每台真实机器节点上保存的记录条数+1
            map.put(node.getIp(), map.get(node.getIp()) + 1);
        }



        // 打印每台真实机器节点保存的记录条数
        for (int i = 1; i <= 10; i++) {
            System.out.println(IP_PREFIX + i + "节点记录条数:" + map.get(IP_PREFIX + i));
        }
    }
}
