package com.mermaid.framework.schedule;

import java.nio.charset.Charset;

public interface Constants {
    String DATA_SOURCE_APP_NAME = "DTS_SERVER_APP";
    String DATA_SOURCE_APP_NAME_META = "DTS_SERVER_META_APP";
    String DB_GROUP_KEY_META = "DTS_SERVER_META_GROUP";
    String SQL_MAP_CONFIG_PATH = "sqlMapConfig.xml";
    String DAILY_ENV_NAME = "daily";
    String EDAS_ENV_NAME = "edas";
    String PERF_ENV_NAME = "perf";
    String PREPUB_ENV_NAME = "prepub";
    String PUBLISH_ENV_NAME = "publish";
    String USA_ENV_NAME = "usa";
    String ALIYUN_ENV_NAME = "aliyun";
    String ALIYUN_TEST_ENV_NAME = "aliyun-test";
    String SH_PREPUB = "上海预发";
    int STORE_TYPE_MYSQL = 0;
    int STORE_TYPE_HBASE = 1;
    String RESOURCE_NAME = "schedulerX";
    String RESOURCE_SEPARATOR = ":";
    int ENVIRONMENT_INNER = 0;
    int ENVIRONMENT_CLOUD = 1;
    Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    String DTS_INI = "dts.ini";
    String FILE_SEPARATOR = "file.separator";
    String USER_HOME = "user.home";
    String DTS_CONFIG = "dtsConfig";
    String JST_DTS_CONFIG = "config";
    String DTS_BASE_SECTION = "baseSection";
    String CONFIG_ITEM_STORE_TYPE = "storeType";
    String CONFIG_ITEM_ENVIRONMENT = "environment";
    String CONFIG_ITEM_LISTENER_PORT = "listenerPort";
    String CONFIG_ITEM_REMOTING_THREADS = "remotingThreads";
    String CONFIG_ITEM_HEART_BEAT_INTERVAL_TIME = "heartBeatIntervalTime";
    String CONFIG_ITEM_HEART_BEAT_CHECK_TIMEOUT = "heartBeatCheckTimeout";
    String CONFIG_ITEM_ZK_HOSTS = "zkHosts";
    String CONFIG_ITEM_ZK_ROOT_PATH = "namespace";
    String CONFIG_ITEM_ZK_SESSION_TIMEOUT = "zkSessionTimeout";
    String CONFIG_ITEM_ZK_CONNECTION_TIMEOUT = "zkConnectionTimeout";
    String CONFIG_ITEM_CLUSTER_ID = "clusterId";
    String CONFIG_ITEM_SERVER_GROUP_ID = "serverGroupId";
    String CONFIG_ITEM_DESCRIPTION = "description";
    String CONFIG_ITEM_JOB_BACKUP_AMOUNT = "jobBackupAmount";
    String CONFIG_ITEM_CHECK_JOB_INTERVAL_TIME = "checkJobIntervalTime";
    String CONFIG_ITEM_COMPENSATION_INTERVAL_TIME = "compensationIntervalTime";
    String CONFIG_ITEM_COMPENSATION_THREADS = "compensationThreads";
    String CONFIG_ITEM_DATA_SOURCE_APP_NAME = "dataSourceAppName";
    String CONFIG_ITEM_DATA_SOURCE_APP_NAME_META = "dataSourceAppNameMeta";
    String CONFIG_ITEM_DB_GROUP_KEY_META = "dbGroupKeyMeta";
    String CONFIG_ITEM_TDDL_APPRULE_FILE = "tddlAppruleFile";
    int DEFAULT_LISTENER_PORT = 52014;
    int REQUEST_CODE = 0;
    int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    int DEFAULT_REMOTING_THREADS = AVAILABLE_PROCESSORS * 4;
    int DEFAULT_COMPENSATION_THREADS = 4;
    String BLANK_SPLIT = " ";
    int HEART_BEAT_THREAD_AMOUNT = 2;
    int CHECK_ZK_THREAD_AMOUNT = 1;
    int COMPENSATION_THREAD_AMOUNT = 1;
    int ZK_SCANNER_THREAD_AMOUT = 1;
    int CHECK_JOB_THREAD_AMOUNT = 1;
    int GC_THREAD_AMOUNT = 1;
    String HEART_BEAT_THREAD_NAME = "DTS-heart-beat-thread-";
    String REMOTING_THREAD_NAME = "DTS-remoting-thread-";
    String CHECK_JOB_THREAD_NAME = "DTS-check-job-thread";
    String CHECK_ZK_THREAD_NAME = "DTS-ZK-check-thread";
    String COMPENSATION_THREAD_NAME = "DTS-compensation-thread";
    String GC_THREAD_NAME = "DTS-gc-thread";
    long DEFAULT_HEART_BEAT_INTERVAL_TIME = 10000L;
    long DEFAULT_HEART_BEAT_CHECK_TIMEOUT = 5000L;
    long DEFAULT_CONNECTION_TIMEOUT = 3000L;
    String DEFAULT_DOMAIN_NAME = "schedulerx.console.aliyun.com";
    String BLANK = " ";
    String NULL = "NULL";
    String SPLIT_POINT = "\\.";
    String POINT = ".";
    String COMMA = ",";
    String COMMA_ENCODED = "%2C";
    String WILDCARD = "*";
    String FORWARD_SLASH = "/";
    String EQUAL_CHAR = "=";
    String COLON = ":";
    String SPLIT_CHAR = "_SPLIT_CHAR_";
    String SPLIT_STRING = "@AND#";
    String HORIZONTAL_LINE = "-";
    String UNDERLINE = "_";
    String DEFAULT_ZK_ROOT_PATH = "zk-dts-root";
    int DEFAULT_ZK_SESSION_TIMEOUT = 10000;
    int DEFAULT_ZK_CONNECTION_TIMEOUT = 10000;
    String ZK_SERVER_CLUSTER = "server-cluster";
    String ZK_CONSOLE_CLUSTER = "console-cluster";
    long DEFAULT_INVOKE_TIMEOUT = 5000L;
    int JOB_TYPE_API_SIMPLE = 0;
    int JOB_TYPE_TIMER_SIMPLE = 1;
    int JOB_TYPE_API_PARALLEL = 2;
    int JOB_TYPE_TIMER_PARALLEL = 3;
    int JOB_TYPE_API_ALL_SIMPLE = 4;
    int JOB_TYPE_TIMER_ALL_SIMPLE = 5;
    int JOB_TYPE_API_LONG_TIME = 6;
    int JOB_TYPE_TIMER_LONG_TIME = 7;
    int JOB_STATUS_DISABLE = 0;
    int JOB_STATUS_ENABLE = 1;
    int JOB_INSTANCE_STATUS_NEVER_FIRED = 0;
    int JOB_INSTANCE_STATUS_NEW = 1;
    int JOB_INSTANCE_STATUS_RUNNING = 2;
    int JOB_INSTANCE_STATUS_FINISHED = 3;
    int JOB_INSTANCE_STATUS_FAILED = 4;
    int JOB_INSTANCE_STATUS_RETRY = 5;
    int JOB_INSTANCE_STATUS_RETRYING = 6;
    int JOB_INSTANCE_STATUS_RETRY_FINISHED = 7;
    int JOB_INSTANCE_STATUS_RETRY_OVER = 8;
    int JOB_INSTANCE_STATUS_DELETE_SELF = 9;
    int TASK_STATUS_INIT = 0;
    int TASK_STATUS_QUEUE = 1;
    int TASK_STATUS_START = 2;
    int TASK_STATUS_SUCCESS = 3;
    int TASK_STATUS_FAILURE = 4;
    int TASK_STATUS_FOUND_PROCESSOR_FAILURE = 5;
    int TASK_STATUS_RUNNING = 6;
    int TASK_STATUS_ALLOCATION = 7;
    int TASK_ACK_FAILURE = 8;
    int TASK_PROCESSOR_STATUS_RUNNING = 1;
    int TASK_PROCESSOR_STATUS_STOP = 0;
    long JOB_INSTANCE_LOCK_TIMEOUT = 10000L;
    long JOB_INSTANCE_LOAD_TIMEOUT = 3000L;
    int DEFAULT_JOB_BACKUP_AMOUNT = 3;
    String DTS_USER = "dtsUser";
    String SERVER_CLUSTER = "serverCluster";
    int PER_PAGE_COUNT = 10;
    long DEFAULT_SERVER_CLUSTER_ID = 1L;
    String TASK_THREAD_NAME = "DtsTaskProcessor-";
    String SCX_TASK_THREAD_NAME = "ScxTaskProcessor-";
    String PULL_TASK_THREAD_NAME = "DtsPullProcessor-";
    String LT_PULL_TASK_THREAD_NAME = "LongTimeDtsPullProcessor-";
    String REFILLING_TASK_THREAD_NAME = "DtsReFillingProcessor-";
    int DEFAULT_PAGE_SIZE = 1000;
    int QUEUE_SIZE = 10000;
    int MAX_TASKLIST_SIZE = 3000;
    int MAX_LONGTIMETASKLIST_SIZE = 10000;
    int DEFAULT_CONSUMER_THREAD_AMOUNT = 5;
    String KEY_LOGING_USER_COOKIE = "login_aliyunid_ticket";
    String KEY_ALIYUN_LOGIN_URL = "aliyunLoginUrl";
    String KEY_ALIYUN_LOGOUT_URL = "aliyunLogoutUrl";
    String DEFAULT_ROOT_LEVEL_TASK_NAME = "defaultTaskName4DtsServerSelf";
    int CHAR_AMOUNT = 16;
    String DEFAULT_TASK_MD5 = "0123456789";
    long DEFAULT_CHECK_JOB_INTERVAL_TIME = 10000L;
    long DEFAULT_COMPENSATION_INTERVAL_TIME = 60000L;
    long DEFAULT_SCANNER_ZK_TIME = 5000L;
    String ZK_JOB_INSTANCE_LIST = "job-instance-list";
    String TIME_FORMAT_SECONDS = "yyyy-MM-dd HH:mm:ss";
    String TIME_FORMAT_HOUR = "yyMMddHH";
    String TIME_FORMAT_CHART = "yyyy-MM-dd HH:mm";
    long DEFAULT_POLL_TIMEOUT = 10000L;
    String JOB_OPERATE_KEY = "operate";
    String JOB_OPERATE_VALUE = "value";
    String JOB_CREATE_OPERATE = "create";
    String JOB_UPDATE_OPERATE = "update";
    String JOB_DELETE_OPERATE = "delete";
    String JOB_INSTANCE_START_OPERATE = "instaceStart";
    String JOB_INSTANCE_STOP_OPERATE = "instaceStop";
    String JOB_ENABLE_OPERATE = "enable";
    String JOB_DISABLE_OPERATE = "disable";
    String JOB_RELATION_CREATE = "createRelation";
    String JOB_RELATION_DELETE = "deleteRelation";
    String DESIGNATED_MACHINE = "designatedMachine";
    String JOB_ID_ITEM = "jobId";
    String JOB_RELATION_ID_ITEM = "jobRelationId";
    String FIRE_TIME_ITEM = "fireTime";
    String FIRE_UNIQUE_ID = "uniqueId";
    int STATUS_STOP = 0;
    int STATUS_RUNNING = 1;
    int MAX_RETRY_COUNT = 100;
    double INCREASE_RATE = 1.5D;
    long START_INTERVAL_TIME = 180000L;
    String TOTAL_PROGRESS = "总体进度";
    long PULL_SLEEP_TIME = 10000L;
    int START_POLICY_SINGLE_INSTANCE = 0;
    int START_POLICY_MULTI_INSTANCE = 1;
    int INVOKE_SOURCE_API = 0;
    int INVOKE_SOURCE_ACK = 1;
    int INVOKE_SOURCE_TIMER = 2;
    String TDS_ALL = "dts-all";
    String SUCCESS = "success";
    String ERROR_MSG = "errMsg";
    String DATA = "data";
    String ACCESS_KEY = "accessKey";
    String SECURITY_KEY = "securityKey";
    String TIME_STAMP = "timestamp";
    String GUID = "GUID";
    String USER_KEY = "userIdKey";
    String ALIYUN_ENVKEY = "aliyunEnv";
    String SIGN = "sign";
    String DEFAULT_GROUP_NAME = "默认分组";
    int DEFAULT_GROUP_SERVER_AMOUNT = 100;
    String ZK_CLIENT_CLUSTER = "client-cluster";
    String ZK_LOCKS = "locks";
    int DESIGNATED_MACHINE_POLICY_MIGTATION = 0;
    int DESIGNATED_MACHINE_POLICY_NOT_MIGTATION = 1;
    String DEFAULT_TDDL_APPRULE_FILE = "dts-tddl-apprule.xml";
    String DTS_SERVER_CONFIG_DATA_ID = "dts_server_config_data_id_2015_02_09";
    int POSITION_PROCESSOR = 0;
    int POSITION_INIT_METHOD = 1;
    int POSITION_BEAN_ID = 2;
    int DEFAULT_MAX_ACTIVE = 100;
    String ENVIRONMENT_JST = "JuShiTa";
    String ENVIRONMENT_SCX = "SchedulerX";
    String ENVIRONMENT_PRIVATE_CLOUD = "PrivateCloud";
    String DTS_CLIENT = "dts-client";
    String DTS_LOGS = "logs";
    String DTS_LOG_EXT = ".log";
    String NEWLINE = "\r\n";
    String ZK_TASK_LIST = "task-list";
    String ZK_HOST_LIST = "host-list";
    int ACTION_START = 1;
    int ACTION_STOP = 0;
}
