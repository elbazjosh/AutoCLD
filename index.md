

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
      <meta name="google-site-verification" content="3-lBLAUi01nCh2234bhGABB3uK4-lZeVGHTTxZfSlYE" />
</head>

# AutoCLD

## Description

AutoCLD is a software meant to avoid resorting to manual methods for creating a causal loop diagram. It automatically spaces and organizes your datausing the JUNG java library. Simply input nodes (variables) and define their relationship to one another (positive/negative) and the software will output the rest. You can manipulate the positioning of the nodes on the generated map, or change their layout and adjust. AutoCLD also utilizes Johnson's elementary cycles algorithm to detect reinforcing and balancing loops. 

## Installation 

Follow the instructions to download the <a href="https://github.com/jrtom/jung">JUNG</a> repository to your Java IDE by clicking the link. 

Download the AutoCLD files by clicking <b><u> Code > Download ZIP </u></b>. Upload the files into your Java IDE Project

### Project Files

The primary project directory is: <code>autocld > src/main/java > autocld_main </code>

Other folders contain duplicates and unfinished drafts of updates for v1.0
<ul>
<li>AutoCLD = executable class </li>
<li>SCCFinder and Cycle = implementation of Johnson's algorithm for loop detection </li>
<li>layouts = different spatial arrangements of nodes </li>
<li>test = duplicate file for testing code </li>
<li>EditListAction, EditEndNode, ListAction = node editing features </li>
<li>LabelAsShape = maintains variable title as node shape </li>
</ul>



## Features

### General Overview

This is a basic example of how AutoCLD accepts inputs of variables and relationships and automatically generates a causal loop figure. The input method is designed to reduce the number of keystrokes for repeated connections, and automatically displays reinforcing and balancing loops when present. Basic spatial editing is also built into the map view.

![](General_overview.gif)

### Editing

You can rename and delete any existing input or end node at any point.

![](Editing.gif)

### Layouts

You can also choose between six different layouts, each of which is designed to orient the various nodes according to different principles. For more information on each layout, follow the thread <a href="http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/algorithms/layout/AbstractLayout.html">here.</a>

![](Layouts.gif)


## Built With

<ul><li><a href="http://jung.sourceforge.net">JUNG</a> - library for directed graphs </li></ul>

<i>Project was developed by Josh Elbaz, 2020</i>
