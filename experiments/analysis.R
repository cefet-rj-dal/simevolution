source("https://raw.githubusercontent.com/eogasawara/mylibrary/master/myGraphics.R")
loadlibrary("reshape")
loadlibrary("RColorBrewer")
loadlibrary("readr")
loadlibrary("readxl")
loadlibrary("writexl")
loadlibrary("dplyr")
loadlibrary("gridExtra")

col.set <- brewer.pal(11, 'Spectral')
likert_l <- c(1, 2, 3, 4)
likert <- c("s-disagree", "disagree", "agree", "s-agree")

cp_l <- c(1, 2, 3, 4)
cp <- c("wrong", "+/- wrong", "+/- right", "right")

cn_l <- c(1, 2, 3, 4)
cn <- c("right", "+/- right", "+/- wrong", "wrong")

yesno_l <- c(0, 1)
yesno <- c("no", "yes")

adjust_yesno <- function(x) {
  x[x <= 2] <- 0
  x[x >= 3] <- 1
  return(x)
}

read_rdata <- function() {
  load("simevolution.RData") 
  
  schema <- read_excel("schema.xlsx", sheet="schema")
  colnames(form) <- schema$column
  form <- form[form$grade=='1º ano do Ensino Médio',]
  form$age <- as.integer(gsub("([0-9]+).*$", "\\1",  form$age))
  form$grade <- as.integer(gsub("([0-9]+).*$", "\\1",  form$grade))
  form <- form[form$grade==1,]

  form$themes_et <- grepl("Teoria da Evolução", form$themes)
  form$themes_nst <- grepl("Seleção Natural", form$themes)
  form$themes_gm <- grepl("Genética Mendeliana", form$themes)
  form$themes_gc <- grepl("Princípios da Genética Clássica", form$themes)
  form$themes_gp <- grepl("Probabilidade Genética", form$themes)
  
  form$birds_forest_none <- grepl("Não houve", form$birds_forest)
  form$birds_forest_color_green <- grepl("Cor verde", form$birds_forest)
  form$birds_forest_color_merged <- grepl("Cor mesclada", form$birds_forest)
  form$birds_forest_color_yellow <- grepl("Cor amarela", form$birds_forest)
  form$birds_forest_beak_pointed <- grepl("Bico em forma de alicate", form$birds_forest)
  form$birds_forest_beak_short <- grepl("Bico grande e fino", form$birds_forest)
  form$birds_forest_beak_curved <- grepl("Bico pequeno", form$birds_forest)

  form$birds_veld_none <- grepl("Não houve", form$birds_veld)
  form$birds_veld_color_green <- grepl("Cor verde", form$birds_veld)
  form$birds_veld_color_merged <- grepl("Cor mesclada", form$birds_veld)
  form$birds_veld_color_yellow <- grepl("Cor amarela", form$birds_veld)
  form$birds_veld_beak_pointed <- grepl("Bico em forma de alicate", form$birds_veld)
  form$birds_veld_beak_short <- grepl("Bico grande e fino", form$birds_veld)
  form$birds_veld_beak_curved <- grepl("Bico pequeno", form$birds_veld)

  form$beak_breding <- grepl("reprodução", form$beak)
  form$beak_predation <- grepl("predação", form$beak)
  form$beak_feeding <- grepl("alimentação", form$beak)

  form$plumage_breding <- grepl("reprodução", form$plumage)
  form$plumage_predation <- grepl("predação", form$plumage)
  form$plumage_feeding <- grepl("alimentação", form$plumage)


  form$rq1 <- factor(form$rq1,levels=cn_l,ordered=TRUE)
  levels(form$rq1) <- cn
  
  form$rq2 <- factor(form$rq2,levels=cp_l,ordered=TRUE)
  levels(form$rq2) <- cp
  
  form$rq3 <- factor(form$rq3,levels=cn_l,ordered=TRUE)
  levels(form$rq3) <- cn
  
  form$rq4 <- factor(form$rq4,levels=cp_l,ordered=TRUE)
  levels(form$rq4) <- cp
  
  form$aid_comprehension  <- factor(form$aid_comprehension,levels=likert_l,ordered=TRUE)
  levels(form$aid_comprehension) <- likert
  
  form$aid_help  <- factor(form$aid_help,levels=likert_l,ordered=TRUE)
  levels(form$aid_help) <- likert
  
  form$aid_visualization  <- factor(form$aid_visualization,levels=likert_l,ordered=TRUE)
  levels(form$aid_visualization) <- likert
  
  form$aid_statistics  <- factor(form$aid_statistics,levels=likert_l,ordered=TRUE)
  levels(form$aid_statistics) <- likert
  
  form$sim_custom <- grepl("Sim", form$sim_custom) 

  form$sim_phenotypes <- factor(form$sim_phenotypes,levels=likert_l,ordered=TRUE)
  levels(form$sim_phenotypes)=likert
  
  form$usage_classes <- grepl("Sim", form$usage_classes) 
  form$usage_count <- as.integer(gsub("([0-9]+).*$", "\\1",  form$usage_count))
  form$usage_count[is.na(form$usage_count)] <- 1
  
  form$usage_error <- grepl("Sim", form$usage_error) 
  
  
  form$usability_easy
  
  form$usability_easy <- adjust_yesno(form$usability_easy)
  form$usability_easy <- factor(form$usability_easy,levels=yesno_l,ordered=TRUE)
  levels(form$usability_easy)=yesno
  
  form$usability_need_theory <- adjust_yesno(form$usability_need_theory)
  form$usability_need_theory <- factor(form$usability_need_theory,levels=yesno_l,ordered=TRUE)
  levels(form$usability_need_theory)=yesno
  
  form$usability_related_functions <- adjust_yesno(form$usability_related_functions)
  form$usability_related_functions <- factor(form$usability_related_functions,levels=yesno_l,ordered=TRUE)
  levels(form$usability_related_functions)=yesno
  
  form$usability_prior_knowledge <- adjust_yesno(form$usability_prior_knowledge)
  form$usability_prior_knowledge <- factor(form$usability_prior_knowledge,levels=yesno_l,ordered=TRUE)
  levels(form$usability_prior_knowledge)=yesno
  
  form <- form %>% select(age, grade, themes_et, themes_nst, themes_gm, themes_gc, themes_gp, 
                          birds_forest_none, birds_forest_color_green, birds_forest_color_merged, birds_forest_color_yellow, birds_forest_beak_pointed, birds_forest_beak_short, birds_forest_beak_curved, 
                          birds_veld_none, birds_veld_color_green, birds_veld_color_merged, birds_veld_color_yellow, birds_veld_beak_pointed, birds_veld_beak_short, birds_veld_beak_curved, 
                          beak_breding, beak_predation, beak_feeding, plumage_breding, plumage_predation, plumage_feeding, 
                          rq1, rq1_detail, rq2, rq2_detail, rq3, rq3_detail, rq4, rq4_detail, 
                          aid_comprehension, aid_help, aid_visualization, aid_statistics, 
                          sim_custom, sim_phenotypes, 
                          usage_count, usage_error, 
                          usability_easy, usability_need_theory, usability_related_functions, usability_prior_knowledge)
  
  return(form)
}

form <- read_rdata()

# Grafico 0
plot_grf_prev_knowledge <- function(color) {
  et <- sum(form$themes_et)/nrow(form)
  nst <- sum(form$themes_nst)/nrow(form)
  gm <- sum(form$themes_gm)/nrow(form)
  gc <- sum(form$themes_gc)/nrow(form)
  gp <- sum(form$themes_gp)/nrow(form)
  
  series <- NULL
  series <- rbind(series, data.frame(x="NS", value=nst))
  series <- rbind(series, data.frame(x="TE", value=et))
  series <- rbind(series, data.frame(x="MG", value=gm))
  series <- rbind(series, data.frame(x="PB", value=gp))
  series <- rbind(series, data.frame(x="PCG", value=gc))
  series$value <- round(series$value*100)
  series$variable <- "Background knowledge"

  grf <- plot.bar(series, group=TRUE, colors=color)
  grf <- grf + xlab("")
  grf <- grf + guides(fill=guide_legend(title="Characterization"))
  grf <- grf + ylab("Percentage(%) of students")
  plot(grf)
}
plot_grf_prev_knowledge(col.set[c(10)])


# Grafico 1.a
plot_grf_birds_colors <- function(colors) {
  n <- nrow(form)
  
  gf <- sum(form$birds_forest_color_green)
  mf <- sum(form$birds_forest_color_merged)
  yf <- sum(form$birds_forest_color_yellow)
  cf <- sum(form$birds_forest_beak_curved)
  sf <- sum(form$birds_forest_beak_short)
  pf <- sum(form$birds_forest_beak_pointed)
  nf <- sum(form$birds_forest_none)
  
  gv <- sum(form$birds_veld_color_green)
  mv <- sum(form$birds_veld_color_merged)
  yv <- sum(form$birds_veld_color_yellow)
  cv <- sum(form$birds_veld_beak_curved)
  sv <- sum(form$birds_veld_beak_short)
  pv <- sum(form$birds_veld_beak_pointed)
  nv <- sum(form$birds_veld_none)

  series <- NULL
  series <- rbind(series, data.frame(x="Green", Forest=gf/n, Veld = gv/n))
  series <- rbind(series, data.frame(x="Merged", Forest=mf/n, Veld = mv/n))
  series <- rbind(series, data.frame(x="Yellow", Forest=yf/n, Veld = yv/n))
  series <- rbind(series, data.frame(x="Curved", Forest=cf/n, Veld = cv/n))
  series <- rbind(series, data.frame(x="Short", Forest=sf/n, Veld = sv/n))
  series <- rbind(series, data.frame(x="Thin&Pointed", Forest=pf/n, Veld = pv/n))
  series <- rbind(series, data.frame(x="None", Forest=nf/n, Veld = nv/n))
  series$Forest <- round(series$Forest*100)
  series$Veld <- round(series$Veld*100)
  series <- melt(series[,c('x','Forest','Veld')],id.vars = 1)
  
  series$variable <- as.character(series$variable)
  series$variable[series$variable == "Forest"] <- "Q1: Forest"
  series$variable[series$variable == "Veld"] <- "Q2: Veld"
  series$variable <- as.factor(series$variable)
  
  grf <- plot.bar(series, group=TRUE, colors=colors)
  grf <- grf + xlab("")
  grf <- grf + guides(fill=guide_legend(title="Environment"))
  grf <- grf + ylab("Percentage(%) of students")
  plot(grf)
}
plot_grf_birds_colors(col.set[c(9,5)])

# Grafico 2
plot_grf_birds_phenotypes <- function(colors) {
  bb <- sum(form$beak_breding)/nrow(form)
  bp <- sum(form$beak_predation)/nrow(form)
  bf <- sum(form$beak_feeding)/nrow(form)
  
  pb <- sum(form$plumage_breding)/nrow(form)
  pp <- sum(form$plumage_predation)/nrow(form)
  pf <- sum(form$plumage_feeding)/nrow(form)
  
  
  series <- NULL
  series <- rbind(series, data.frame(x="Feeding", BeakType=bf, Plumage = pf))
  series <- rbind(series, data.frame(x="Breeding", BeakType=bb, Plumage = pb))
  series <- rbind(series, data.frame(x="Predation", BeakType=bp, Plumage = pp))
  series$BeakType <- round(series$BeakType * 100)
  series$Plumage <- round(series$Plumage * 100)
  series <- melt(series[,c('x','BeakType','Plumage')],id.vars = 1)
  series$variable <- as.character(series$variable)
  series$variable[series$variable == "BeakType"] <- "Q3: Beak shape"
  series$variable[series$variable == "Plumage"] <- "Q4: Plumage color"
  series$variable <- as.factor(series$variable)  

  grf <- plot.bar(series, group=TRUE, colors=colors)
  grf <- grf + xlab("")
  grf <- grf + guides(fill=guide_legend(title="Characteristic"))
  grf <- grf + ylab("Percentage(%) of students")
  plot(grf)
}
plot_grf_birds_phenotypes(col.set[c(1,4)])


# Grafico 3
plot_grf_question <- function(colors_yes, colors_no) {
  series <- form %>% select(rq1) %>% group_by(variable=rq1)  %>% summarize(value = n())
  series$colors <- colors_no
  series <- prepare.pieplot(series)
  grfQ5 <- plot.pieplot(series, label_y = "Q5", colors=as.character(series$colors))
  
  series <- form %>% select(rq2) %>% group_by(variable=rq2)  %>% summarize(value = n())
  series$colors <- colors_yes
  series <- prepare.pieplot(series)
  grfQ6 <- plot.pieplot(series, label_y = "Q6", colors=as.character(series$colors))
  
  series <- form %>% select(rq3) %>% group_by(variable=rq3)  %>% summarize(value = n())
  series$colors <- colors_no
  series <- prepare.pieplot(series)
  grfQ7 <- plot.pieplot(series, label_y = "Q7", colors=as.character(series$colors))
  
  series <- form %>% select(rq4) %>% group_by(variable=rq4)  %>% summarize(value = n())
  series$colors <- colors_yes
  series <- prepare.pieplot(series)
  grfQ8 <- plot.pieplot(series, label_y = "Q8", colors=as.character(series$colors)) 
  
  grid.arrange(grfQ5, grfQ6, grfQ7, grfQ8, ncol=4)  

#  plot(grfpie)  

#  series <- rbind(series, form %>% select(rq2) %>% group_by(variable=rq2)  %>% summarize(x="Q6", value = n()))
#  series <- rbind(series, form %>% select(rq3) %>% group_by(variable=rq3)  %>% summarize(x="Q7", value = n()))
#  series <- rbind(series, form %>% select(rq4) %>% group_by(variable=rq4)  %>% summarize(x="Q8", value = n()))
}
plot_grf_question(col.set[c(3,5,7,9)], col.set[c(9,7,5,3)])


# Grafico 4
plot_grf_aid <- function(color) {
  x <- NULL
  x <- rbind(x, form %>% select(sim_custom) %>% summarize(x=6, value = sum(sim_custom)*100/nrow(form)))
  x <- rbind(x, form %>% select(sim_phenotypes) %>% filter(sim_phenotypes %in% (likert[3:4]))  %>% summarize(x=5, value = n()*100/nrow(form)))
  x <- rbind(x, form %>% select(aid_statistics) %>% filter(aid_statistics %in% (likert[3:4]))  %>% summarize(x=4, value = n()*100/nrow(form)))
  x <- rbind(x, form %>% select(aid_visualization) %>% filter(aid_visualization %in% (likert[3:4]))  %>% summarize(x=3, value = n()*100/nrow(form)))
  x <- rbind(x, form %>% select(aid_help) %>% filter(aid_help %in% (likert[3:4]))  %>% summarize(x=2, value = n()*100/nrow(form)))
  x <- rbind(x, form %>% select(aid_comprehension) %>% filter(aid_comprehension %in% (likert[3:4]))  %>% summarize(x=1, value = n()*100/nrow(form)))
  
  colnames(x) <- c("x", "y")
  x$y <- round(x$y)
  x$x <- as.factor(x$x)
  levels(x$x)=c("Q14: Simulation setup", "Q13: Phenotypes setup", "Q12: Statistics", "Q11: Visualization", "Q10: Help system", "Q9: Comprehension")  

  # Horizontal lollipop
  grf <- ggplot(data=x, aes(x=x, y=y, label=y)) +
    geom_segment( aes(x=x, xend=x, y=0, yend=(y-0.25)), color=color, size=1) +
    geom_text(color="black", size=3) +
    geom_point( color=color, size=8, alpha=0.2) +
    theme_light() +
    coord_flip() +
    theme(
      panel.grid.major.y = element_blank(),
      panel.border = element_blank(),
      axis.ticks.y = element_blank()
    ) +
    ylab("Percentage(%) of students agreeing") + xlab(" ")   
  plot(grf)  #4x3  
}
plot_grf_aid(col.set[c(11)])

plot_usability <- function(colors) {
  series <- NULL
  series <- rbind(series, form %>% select(usability_easy) %>% group_by(variable=usability_easy)  %>% summarize(x="Q15", value = n()))
  series <- rbind(series, form %>% select(usability_need_theory) %>% group_by(variable=usability_need_theory)  %>% summarize(x="Q16", value = n()))
  series <- rbind(series, form %>% select(usability_related_functions) %>% group_by(variable=usability_related_functions)  %>% summarize(x="Q17", value = n()))
  series <- rbind(series, form %>% select(usability_prior_knowledge) %>% group_by(variable=usability_prior_knowledge)  %>% summarize(x="Q18", value = n()))
  
  series$value <- series$value *100/nrow(form)

  grf <- plot.bar(series, group=TRUE, colors=colors)
  grf <- grf + xlab("")
  grf <- grf + guides(fill=guide_legend(title="Answer"))
  grf <- grf + ylab("Percentage(%) of students")
  plot(grf)
}
plot_usability(col.set[c(3,9)])


table_summarize <- function() {
  series <- NULL
  series <- rbind(series, form %>% select(usage_error) %>% summarize(variable="Found errors", value = sum(usage_error)*100/nrow(form)))
  series <- rbind(series, form %>% select(usage_count) %>% summarize(variable="Usage", value = mean(usage_count, na.rm = TRUE)))
  series <- rbind(series, form %>% select(age) %>% summarize(variable="Age", value = mean(age, na.rm = TRUE)))
  return(series)
}
usage <- table_summarize()
print(usage)