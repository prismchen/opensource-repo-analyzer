# opensource-repo-analyzer
  This project is about analyzing texts of Java source code on GitHub on the following 4 aspects: indentation style, bracket style, ratio of empty lines and most popular Apache-project imports.

## Data Source 
  GitHub files stored in Google BigQuery table: bigquery-public-data:github_repos.sample_contents, which includes 10% random sampled text contents of top 400k GitHub repositories with more than 2 stars received during Jan-May 2016, roughly 260k java files in total.

## Indentation Style
  A mapreduce job analyzes the indentaion style of java codes. The result is presented as numbers of votes to each indentation style (tab, 2 spaces, 4 spaces, etc.) and each unique file contributes to one vote. More specific rule is that a indentation style is either tab or the least number of spaces that is noticeably used for indenting (exceeds 2% of all indented lines).
  
  A brief view of results (descending order):
  
		4 spaces:   135727/260784 (52%)
		Tab:        72246/260784 (28%)
		2 spaces:   41748/260784 (16%)
		No indent:  6636/260784 (3%)
		Others:                 (1%)
		
## Bracket Style
  A mapreduce job that analyzes the style of curly brackets in Java code. The possible styles are 
  	
	Inline bracket, e.g.
		for (int i = 0; i < n; i++) {
		    ...
		}
		
	Next line bracket, e.g. 
		for (int i = 0; i < n; i++) 
		{
		    ...
		}
		
  A specific bracket style for a file is determined by compare occurrence of two styles, or "unclear" if a tie or not occurrence at all.
  Followings are the results (descending order):
  
		Inline:    220333/260784 (85%)
		Next line: 36957/260784 (14%)
		Unclear:   3494/260784 (1%)
		

## Ratio of Empty Lines
  A mapreduce job analyze numbers of empty lines in Java code files, in the form of (# of empty lines)/(# of total lines)
  Followings are the results (accumulative):
  
		< 5.0 %         28741/260784 (11%)
		< 10.0 %        28147/260784 (22%)
		< 15.0 %        77275/260784 (52%)
		< 20.0 %        67674/260784 (78%)
		< 25.0 %        34217/260784 (91%)
		< 30.0 %        14000/260784 (96%)

## Popular Apache-project Imports 
  A mapreduce job that records each Apache-project import in all Java code files, and outputs total imports for each Apache project 
  Followings are the results as number of importing files against total files (top 20, descending order):
  
		org.apache.hadoop:              3162/260784
		org.apache.log4j:               2481/260784
		org.apache.commons.lang:        2220/260784
		org.apache.commons.logging:     2041/260784
		org.apache.camel:               1512/260784
		org.apache.commons.io:          1509/260784
		org.apache.ignite:              808/260784
		org.apache.wicket:              740/260784
		org.apache.cassandra:           615/260784
		org.apache.maven:               506/260784
		org.apache.jackrabbit:          466/260784
		org.apache.jena:                465/260784
		org.apache.uima:                433/260784
		org.apache.thrift:              384/260784
		org.apache.sling:               384/260784
		org.apache.isis:                338/260784
		org.apache.commons.math:        336/260784
		org.apache.activemq:            332/260784
		org.apache.commons.codec:       322/260784
		org.apache.zookeeper:           314/260784
		
