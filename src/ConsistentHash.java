import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {

    // hash函数接口
    private final HashFunction hashFunction;
    // 每个机器节点关联的虚拟节点个数
    private final int numberOfReplicas;
    // 环形虚拟节点
    private final SortedMap<Long, T> circle = new TreeMap<>();

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * 增加真实机器节点，同时添加虚拟节点
     * 虚拟节点key = IP#i，比如真实机器ip为127.0.0.1，则虚拟节点key=127.0.0.1#1
     *
     * @param node node
     */
    private void add(T node) {
        for (int i = 0; i < this.numberOfReplicas; i++) {
            circle.put(this.hashFunction.hash(node.toString() + "#" + i), node);
        }
    }

    /**
     * 删除真实机器节点，同时删除虚拟节点
     *
     * @param node node
     */
    public void remove(T node) {
        for (int i = 0; i < this.numberOfReplicas; i++) {
            circle.remove(this.hashFunction.hash(node.toString() + "#" + i));
        }
    }

    /**
     * 取得真实机器节点
     *
     * @param key key
     * @return T
     */
    public T get(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        long hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {
            // 沿环的顺时针找到一个虚拟节点
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        // 返回该虚拟节点对应的真实机器节点的信息
        return circle.get(hash);
    }
}
