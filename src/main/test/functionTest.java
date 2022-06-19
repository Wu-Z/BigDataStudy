import com.flink.jerry.util.StringUtil;
import com.google.gson.Gson;
import com.flink.jerry.trigger.model.UserVisitWebEvent;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.*;

public class functionTest {

    @Test
    public void jodaTest(){
        String t1 = LocalDateTime.now().toString();
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        System.out.println(yyyyMMdd.format(LocalDateTime.now()));
    }

    @Test
    public void gson(){
        UserVisitWebEvent user = UserVisitWebEvent.builder()
                .id("1")
                .date("20220201")
                .url("url")
                .pageId(2)
                .userId("123")
                .build();
        String s = new Gson().toJson(user);
        System.out.println(s);
    }

    @Test
    public void hash(){System.out.println("console-consumer-84564".hashCode()%50);}

    // http post 请求
    @Test
    public void httpClientTest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/robot/send?access_token=7dfef9217014d17b000a6f270de0ec5a11db7f984886e402b347eb96317c90f5");
        httpPost.setHeader("Content-Type","application/json");
        httpPost.setEntity(new StringEntity("{\"msgtype\": \"text\",\"text\": {\"content\":\"-----\"}}"));

        /*
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("msgtype", "text"));
            nvps.add(new BasicNameValuePair("text", "{\"content\":\"我就是我, @XXX 是不一样的烟火\"}"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        */

        System.out.println(httpPost.getEntity());

        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            System.out.println(response2.getStatusLine());

            InputStream content = response2.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println(response);
            in.close();

            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
    }

    @Test
    public void futureTest() throws ExecutionException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuffer stringBuffer = new StringBuffer("33");

        stringBuffer.insert(1,1);

        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
    }

    @Test
    public void weiTest(){
        System.out.println(Integer.MAX_VALUE);
        int i = 1 << 30;
        System.out.println(i + "\t1 << 30");
        int j =1 << 33;
        System.out.println(j + "\t1 << 33");
        int k = 1 << 64;
        System.out.println(k + "\t1 << 64");
        int l =1 << 65;
        System.out.println(l + "\t1 << 65");
        int m = 2 << 2;
        System.out.println(m + "\t2 << 2");

        int maxValue = Integer.MAX_VALUE;

    }

    @Test
    public void testMap(){
        HashMap<Integer, String> map = new HashMap<>(1,10);
        for (int i = 0; i < 9; i++) {
            map.put(i,"3");
        }

        System.out.println(map);
    }
    @Test
    public void tempTest(){
        System.out.println(UUID.randomUUID());
    }
    @Test
    public void exceptionTest(){
        int i = 2 ;
        assert i==1: "必须为1";
        System.out.println("结束");
    }
    @Test
    public void tet(){
        TreeSet<String> strings = new TreeSet<>();
        for (String s : new String[]{"吉林市", "长春市", "绵阳市", "六安市", "合肥市", "宿州市", "芜湖市", "蚌埠市", "济宁市", "菏泽市", "忻州市", "云浮市", "广州市", "潮州市", "珠海市", "阳江市", "韶关市", "桂林市", "乌鲁木齐市", "南京市", "泰州市", "盐城市", "镇江市", "赣州市", "保定市", "唐山市", "石家庄市", "衡水市", "安阳市", "郑州市", "宁波市", "杭州市", "绍兴市", "衢州市", "岳阳市", "湘潭市", "益阳市", "衡阳市", "陇南市", "厦门市", "莆田市", "拉萨市", "重庆郊县", "宝鸡市", "渭南市", "西安市", "中山市", "中山市", "吉安市", "杭州市", "南昌市", "北京市", "成都市", "杭州市", "天津市", "太原市", "舟山市", "成都市", "吴忠市", "合肥市", "青岛市", "东莞市", "广州市", "南宁市", "洛阳市", "宁波市", "绍兴市", "武汉市", "信阳市", "洛阳市", "东方市", "万宁市", "文昌市", "琼海市", "三亚市", "淄博市", "淄博市", "深圳市", "深圳市", "常德市", "株洲市", "湘潭市", "益阳市", "荆州市", "舟山市", "舟山市", "岳阳市", "衡阳市", "上海市", "曲靖市", "成都市", "绵阳市", "潍坊市", "广州市", "惠州市", "玉林市", "苏州市", "沧州市", "嘉兴市", "杭州市", "衡阳市", "漳州市", "遵义市", "沈阳市", "重庆城区", "上海市", "昆明市", "南充市", "宜宾市", "德阳市", "攀枝花市", "泸州市", "达州市", "阿坝藏族羌族自治州", "雅安市", "阜阳市", "临沂市", "威海市", "日照市", "济南市", "淄博市", "烟台市", "青岛市", "大同市", "佛山市", "梅州市", "汕头市", "汕尾市", "江门市", "深圳市", "南宁市", "玉林市", "钦州市", "无锡市", "苏州市", "南昌市", "廊坊市", "张家口市", "秦皇岛市", "开封市", "新乡市", "丽水市", "台州市", "嘉兴市", "湖州市", "金华市", "宜昌市", "荆州市", "襄阳市", "鄂州市", "黄石市", "娄底市", "常德市", "株洲市", "泉州市", "福州市", "山南市", "大连市", "沈阳市", "西宁市", "深圳市", "南昌市", "广州市", "北京市", "北京市", "南京市", "南京市", "周口市", "长沙市", "天津市", "天津市", "舟山市", "太原市", "上海市", "昆明市", "成都市", "银川市", "烟台市", "太原市", "中山市", "惠州市", "江门市", "南京市", "无锡市", "苏州市", "郑州市", "杭州市", "温州市", "长沙市", "重庆城区", "咸阳市", "汉中市", "西安市", "九江市", "洛阳市", "万宁市", "琼海市", "琼海市", "万宁市", "三亚市", "琼海市", "三亚市", "琼海市", "琼海市", "淄博市", "深圳市", "深圳市", "深圳市", "深圳市", "娄底市", "张家界市", "怀化市", "永州市", "邵阳市", "郴州市", "兰州市", "兰州市", "舟山市", "荆门市", "衡阳市", "衡阳市", "衡阳市", "衡阳市", "衡阳市", "娄底市", "衡阳市", "昆明市", "合肥市", "东莞市", "佛山市", "南京市", "无锡市", "上饶市", "唐山市", "石家庄市", "台州市", "宁波市", "岳阳市", "长沙市", "曲靖市", "北京市", "乐山市", "内江市", "巴中市", "广安市", "成都市", "眉山市", "自贡市", "遂宁市", "天津市", "潍坊市", "聊城市", "太原市", "东莞市", "中山市", "惠州市", "清远市", "湛江市", "柳州市", "贵港市", "南通市", "宿迁市", "常州市", "徐州市", "扬州市", "连云港市", "九江市", "洛阳市", "温州市", "舟山市", "海口市", "十堰市", "孝感市", "武汉市", "长沙市", "兰州市", "天水市", "平凉市", "漳州市", "贵阳市", "重庆城区", "咸阳市", "铜川市", "哈尔滨市", "中山市", "中山市", "深圳市", "深圳市", "广州市", "深圳市", "北京市", "南京市", "南京市", "周口市", "周口市", "天津市", "太原市", "太原市", "开封市", "北京市", "杭州市", "北京市", "天津市", "固原市", "佛山市", "揭阳市", "深圳市", "信阳市", "嘉兴市", "天水市", "沈阳市", "安康市", "海口市", "海口市", "海口市", "三亚市", "海口市", "深圳市", "深圳市", "岳阳市", "衡阳市", "长沙市", "长沙市", "长沙市", "湛江市", "荆州市", "舟山市", "荆门市", "西安市", "深圳市", "北京市", "长春市", "天津市", "济南市", "济宁市", "青岛市", "深圳市", "南宁市", "常州市", "淮安市", "郑州市", "武汉市", "泉州市", "福州市", "大连市", "重庆郊县", "黄山市"}) {
            strings.add(s);
        }

        System.out.println(strings);
    }

    @Test
    public void test(){
        System.out.println(TimeUnit.SECONDS.toSeconds(10));
    }

    @Test
    public void wei(){

        // 取出最右的1
        int i = 20;
        int rightOne = i & (~i + 1);
        System.out.println(rightOne);

    }

    @Test
    public void test2(){
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int j = random.nextInt(1);
            System.out.println(j);
        }
    }

    @Test
    public void test3(){
        for (int i = 0; i < 200; i++) {
            System.out.println(UUID.randomUUID());
        }
    }

    @Test
    public void fileRead() throws IOException {

        File file = new File("src/main/test/flink2/file/uuidList.txt");
        Reader reader = null;

        try {

            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }

        }
        catch (Exception e){e.printStackTrace();}
        finally {reader.close();}

    }

    @Test
    public void  test11(){

        int i = 1;

        String data = "1653553536,127.0.0.1,3b3a9730-3e11-443f-a731-1b43df1104b1,1,1,2,3,244,2,1,C1804111843,2002,7,1652868489,0,1073923,40312,1652868489,1652868489";
        for (String s : data.split(",")) {
            System.out.println(i+"\t"+s);
            i++;
        }

    }

    @Test
    public void test33(){
        System.out.println(new Random().nextInt(2));
    }

    @Test
    public void test22(){

        System.out.println(StringUtil.getRandomString(5));

    }

}


