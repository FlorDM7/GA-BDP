# Experimenten van G&A plotten
results <- read.table("C:/Users/Flor De Meulemeester/JavaProjects/GA-BDP/results3percent.txt", quote="\"", comment.char="")
results <- subset(results, select = -c(V2,V4,V6,V8,V10,V12,V14,V16,V18,V20))
results <- data.frame(t(results))
colnames(results) <- c("Col1", "Col2", "Col3", "Col4", "Col5", "Col6", "Col7")
View(results)

nodes = seq(10,100,by=10)
nodes
barycenter = results$Col1
barycenter
splitting = results$Col2
TS10 = results$Col3
TS50 = results$Col4
# TS100 = results$Col5
# TS250 = results$Col6
TS1000 = results$Col5
TS2000 = results$Col6
TS5000 = results$Col7

x = nodes
# Create an empty plot
plot(x, TS5000, type = "n", xlim = c(10, 100), ylim=c(0,100),
     xlab = "#knopen", ylab = "gemiddeld #kruisingen verbeterd (%)", main = "Derde experiment")
# Plot each line one by one
lines(x, barycenter, type = "l", col = "red")
lines(x, barycenter, type = "p", col = "red")
lines(x, splitting, type = "l", col = "blue")
lines(x, splitting, type = "p", col = "blue")
lines(x, TS50, type = "l", col = "green")
lines(x, TS50, type = "p", col = "green")
lines(x, TS5000, type = "l", col = "black")
lines(x, TS5000, type="p", col = "black")
# Add a legend
legend("bottomleft", legend = c("BC", "SP", "TS(all)", "TS(next)"), 
       col = c("red", "blue", "green", "black"), lty = 1)

