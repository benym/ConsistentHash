/**
 * 物理机节点模拟类，保存节点的IP，名称，端口等信息
 *
 * @param <T> T
 */
public class Node<T> {
    // IP
    private String ip;
    // 名称
    private String name;

    public Node(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 复写toString方法，使用节点IP当做hash的Key
     *
     * @return ip
     */
    @Override
    public String toString() {
        return ip;
    }
}
