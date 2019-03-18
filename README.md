### a simple offline log parser 
简介：用来处理用户点击日志的spark批处理程序，结果用于推荐效果分析，推荐模型优化等等。本程序基于视频推荐。
输入：hdfs上日志存放路径
输出：mongodb
实时日志处理可在此基础上采用spark-stream，kafka，redis结合的形式。重构用户特征。