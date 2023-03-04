# Logging
This repository contains a ktor application and an associated postgres database.
Logs produced in both components are collected, stored and analyzed using Filebeat, Logstash, Elasticsearch and Kibana (also known as ELK-Stack).

##### Filebeat
- File monitoring and shipping
    - Files are read line by line and sent to the destination (Logstash / Elasticsearch)
    - Remembers state of the files (what was already read)

##### Logstash
- Data ingestion: 
    - Collection 
    - Transformation (structure, format,..)
    - Transmission to the destination

##### Elasticsearch
- Search engine platform (full text search)
- Distributed document storage to store log entries
- Manual / automated data analysis 
    - Manual queries
    - Anomaly detection
    - Identification of unusual events and behaviors

##### Kibana
- Visualization tool
    - Graphs
    - Queries (integrated with elasticsearch)
    

## Summary
Summary based on [1], [2], [3], [6], [7], [8], [9], [10]:
##### Identification of incidents
- Types of incidents
  - Security (e.g. brute force attacks, data interception)
  - Policy violations
- Identification of incidents: Establishing baselines, it is possible to detect anomalies and therefore potential incidents based on them.

##### Debugging
- Logs provide information about problems, incidents and unusual conditions.

##### Compliance
- Audit trails (What happens to data? --> CRUD)
  - e.g. Financial transactions, 
- Data that might be subject to subsequent requests
  - Regulatory necessity to provide personal data
  - Police investigations

##### Monitoring
- Business process monitoring
- Anti-automation monitoring
- Performance monitoring (latencies,..)

## Considerations
##### What to log? --> Events!
e.g.
- Failures
    - Input validation
    - Output validation
    - Authentication
    - Authorization
    - Session management
    - Application errors
- Important transactions & functionality
- Legally relevant events

##### What should not be logged / what should be masked?
- Data
    - Personal data
    - Data that is illegal to collect
    - Sensitive information
- Security-relevant data
    - Access tokens
    - Passwords
    - Secrets
- Source code

##### What to include in a log?
- When: Date, time
- Where: Location (Identifiers to identify the application, the location within the application code and so on)
- Who: Relevant actors (user identifier, source address)
- What: Type of the event, severity, whether it is security relevant, log message / description

##### How to log?
- Use existing logging mechanisms / frameworks
- Validate & sanitize log entries
    - Ensure correct format
    - Prevent log injection attacks
    - Prevent SQL injection attacks (if logs are stored in a database)
- Consider time differences between different applications

Summary of the slf4j [3] logging levels:

| Name  | Description                                             |
| ----- | ------------------------------------------------------- |
| Info  | Information indicating the progress of the application. |
| Debug | Fine grained, used to debug applications                |
| Trace | Information finer grained than the debug level.|
| Error | Error events, allowing the application to continue running.   |
| Fatal | Severe error events, likely crashing the application.         |

##### How to manage logs?
- File system
- Database
- Software to aggregate, analyze and monitor logs
    - Elasticsearch, Logstash, Kibana (ELK) stack
    - Prometheus
    - OpenSearch

## Associated attacks
- Retrieval of sensitive information stored in logs (CWE-532: Insertion of Sensitive Information into Log File [6])
- Flooding of log files
    - Reduces available disk space
        - Impacts other applications
        - Reduces disk space available for further logs
- Injection attacks (CWE-117: Improper Output Neutralization for Logs [10], CWE-117: Improper Output Neutralization for Logs [10])
    - Manipulation / deletion of existing log entries
    - Insertion of corrupted log entries
    - Code injection / execution by log injection
- Unsuitable amount of logging (too much / to little)
    - CWE-779: Logging of Excessive Data [8]
    - CWE-778: Insufficient Logging: Insufficient logging [7]
    - CWE-223: Omission of Security-relevant Information [9]

[1]: https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html
[2]: https://owasp.org/www-community/attacks/Log_Injection
[3]: https://www.slf4j.org/api/org/apache/log4j/Level.html
[4]: https://www.elastic.co/guide/en/elasticsearch/reference/current/elasticsearch-intro.html
[5]: https://www.elastic.co/guide/en/beats/filebeat/current/how-filebeat-works.html
[6]: https://cwe.mitre.org/data/definitions/532.html
[7]: https://cwe.mitre.org/data/definitions/778.html
[8]: https://cwe.mitre.org/data/definitions/779.html
[9]: https://cwe.mitre.org/data/definitions/223.html
[10]: https://cwe.mitre.org/data/definitions/117.html
