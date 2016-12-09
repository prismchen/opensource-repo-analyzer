import java.util.*;

public class PrefixTree {

	public static final String PORJECT_LIST = 
		"NET Ant Library;" + 
		"Abdera;" + 
		"Accumulo;" + 
		"ACE;" + 
		"ActiveMQ;" + 
		"Airavata;" + 
		"Ambari;" + 
		"Anakia;" + 
		"Ant;" + 
		"AntUnit;" + 
		"Any23;" + 
		"Apex;" + 
		"Archiva;" + 
		"Aries;" + 
		"Avro;" + 
		"Axiom;" + 
		"Axis2;" + 
		"Beehive ;" + 
		"Bigtop;" + 
		"BookKeeper;" + 
		"Brooklyn;" + 
		"BVal;" + 
		"Calcite;" + 
		"Camel;" + 
		"Cassandra;" + 
		"Cayenne;" + 
		"Chainsaw;" + 
		"Chemistry;" + 
		"Chukwa;" + 
		"Clerezza;" + 
		"Click;" +  
		"CloudStack;" + 
		"Cocoon;" + 
		"Commons BCEL;" + 
		"Commons BeanUtils;" + 
		"Commons BSF;" + 
		"Commons Chain;" + 
		"Commons CLI;" + 
		"Commons Codec;" + 
		"Commons Collections;" + 
		"Commons Compress;" + 
		"Commons Configuration;" + 
		"Commons Daemon;" + 
		"Commons DBCP;" + 
		"Commons DbUtils;" + 
		"Commons Digester;" + 
		"Commons Discovery;" + 
		"Commons EL;" + 
		"Commons Email;" + 
		"Commons Exec;" + 
		"Commons FileUpload;" + 
		"Commons Functor;" + 
		"Commons HttpClient;" + 
		"Commons IO;" + 
		"Commons JCI;" + 
		"Commons JCS;" + 
		"Commons Jelly;" + 
		"Commons JEXL;" + 
		"Commons JXPath;" + 
		"Commons Lang;" + 
		"Commons Launcher;" + 
		"Commons Logging;" + 
		"Commons Math;" + 
		"Commons Modeler;" + 
		"Commons Net;" + 
		"Commons OGNL;" + 
		"Commons Pool;" + 
		"Commons Proxy;" + 
		"Commons SCXML;" + 
		"Commons Validator;" + 
		"Commons VFS;" + 
		"Commons Weaver;" + 
		"Compress Ant Library;" + 
		"Continuum ;" + 
		"Cordova;" + 
		"Crunch;" + 
		"cTAKES;" + 
		"Curator;" + 
		"CXF;" + 
		"DataFu ;" + 
		"DeltaSpike;" + 
		"Derby;" + 
		"DeviceMap;" + 
		"DirectMemory ;" + 
		"Directory;" + 
		"Directory Server;" + 
		"Directory Studio;" + 
		"Drill;" + 
		"ECS;" + 
		"Edgent;" + 
		"Empire-db;" +
		"Etch;" + 
		"Excalibur;" + 
		"Falcon;" + 
		"Felix;" + 
		"Flink;" + 
		"Flume;" + 
		"FOP;" + 
		"Forrest;" + 
		"Fortress;" + 
		"FtpServer;" + 
		"Geronimo;" + 
		"Giraph;" + 
		"Gora;" + 
		"Groovy;" + 
		"Hadoop;" + 
		"Hama;" + 
		"Harmony ;" + 
		"HBase;" + 
		"Helix;" + 
		"Hive;" + 
		"Hivemind ;" + 
		"HttpComponents Client;" + 
		"HttpComponents Core;" + 
		"Ignite;" + 
		"Isis;" + 
		"Ivy;" + 
		"IvyDE;" + 
		"Jackrabbit;" + 
		"Jakarta Cactus;" + 
		"JAMES;" + 
		"jclouds;" + 
		"Jena;" + 
		"JMeter;" + 
		"JSPWiki;" + 
		"Karaf;" + 
		"Kerby;" + 
		"Knox;" + 
		"Lenya;" + 
		"log4j;" + 
		"Lucene Core;" + 
		"Mahout;" + 
		"ManifoldCF;" + 
		"Marmotta;" + 
		"Maven;" + 
		"Maven Doxia;" + 
		"MetaModel;" + 
		"MINA;" + 
		"MRUnit;" + 
		"MyFaces;" + 
		"Nutch;" + 
		"ODE;" + 
		"OFBiz;" + 
		"Olingo;" + 
		"Oltu - Parent;" + 
		"OODT;" + 
		"Oozie;" + 
		"OpenJPA;" + 
		"OpenMeetings;" + 
		"OpenNLP;" + 
		"OpenWebBeans;" + 
		"ORC;" + 
		"ORO;" + 
		"Parquet;" + 
		"PDFBox;" + 
		"Phoenix;" + 
		"Pig;" + 
		"Pivot;" + 
		"POI;" + 
		"Portals;" + 
		"Props Ant Library;" + 
		"Qpid;" + 
		"Rat;" + 
		"REEF;" + 
		"Regexp ;" + 
		"River;" + 
		"Roller;" + 
		"Sandesha2;" + 
		"Santuario;" + 
		"Scout;" + 
		"ServiceMix;" + 
		"Shale;" + 
		"Shindig;" + 
		"Shiro;" + 
		"Sling;" + 
		"Solr;" + 
		"Spark;" + 
		"Spatial Information System;" + 
		"Sqoop;" + 
		"SSHD;" + 
		"Stanbol;" + 
		"Storm;" + 
		"Stratos;" + 
		"Struts;" + 
		"Synapse;" + 
		"Syncope;" + 
		"Tajo;" + 
		"Tapestry;" + 
		"Taverna;" + 
		"Tentacles;" + 
		"Texen;" + 
		"Tez;" + 
		"Thrift;" + 
		"Tika;" + 
		"Tiles;" + 
		"Tobago;" + 
		"Tomcat;" + 
		"TomEE;" + 
		"Torque;" + 
		"Turbine;" + 
		"Tuscany;" + 
		"UIMA;" + 
		"Velocity;" + 
		"Velocity DVSL;" + 
		"Velocity Tools;" + 
		"VSS Ant Library;" + 
		"VXQuery;" + 
		"Vysper;" + 
		"Whirr;" + 
		"Whisker;" + 
		"Wicket;" + 
		"Wink;" + 
		"Woden;" + 
		"Wookie;" + 
		"Xalan for Java XSLT Processor;" + 
		"Xerces for Java XML Parser;" + 
		"Xindice;" + 
		"XML Commons External;" + 
		"XML Commons Resolver;" + 
		"XML Graphics Commons;" + 
		"XMLBeans;" + 
		"Yetus;" + 
		"Zeppelin;" + 
		"Zest;" + 
		"ZooKeeper";

	class Node {
		Map<String, Node> children;
		Boolean isWord;

		public Node() {
			children = new HashMap<>();
			isWord = false;
		}
	}

	Node root = new Node();

	public PrefixTree() {
		buildTree();
	}

	private void buildTree() {
		String[] projects = PORJECT_LIST.split("\\;");
		for (String project : projects) {
			insert(project, root);
		}
	}

	private void insert(String project, Node root) {
		String[] words = project.split("\\s");
		for (String word : words) {
			word = word.toLowerCase();
			if (!root.children.containsKey(word)) {
				root.children.put(word, new Node());
			}
			root = root.children.get(word);
		}
		root.isWord = true;
	}


	public String search(String[] words) {
		Node curt = root;
		String searchResult = "org.apache";
		for (String word : words) {
			word = word.toLowerCase();
			if (curt.isWord) {
				return searchResult;
			}
			if (curt.children.containsKey(word)) {
				searchResult += "." + word;
				curt = curt.children.get(word);
			}
			else {
				return null;
			}
		}
		return null;
	}

}