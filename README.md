# opensource-repo-analyzer
## SpaceCount
  
SpaceCount is a mapreduce job for analyzing indentaion style of java codes. I applied it to analyze all java files in
Google's BigQuery table "bigquery-public-data:github_repos.sample_contents" - over 260k java files in total. The analysis
result is presented as the number of votes to each indentation style (tab, 2 spaces, 4 spaces, etc.) and each unique file
contributes to one vote. 
	
	A brief view of result:
	
		Tab: 		72246 - 27.7%
		2 Space:	172695 - 66.22%
		4 Space: 	15843 - 6.08%
