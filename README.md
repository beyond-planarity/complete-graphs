Instructions for using the program
======
With this programm, you can create all drawings for complete and complete bipartite graphs for a certain graph class.
Also negative results can be obtained (no drawing possible for a certain graph).

Currently the constraints for 5 graph classes are implemented:
- Fan-crossing Free Graphs
- Fan-planar Graphs
- K-planar Graphs
- Quasi-planar Graphs
- Gap-planar Graphs

For negative results, use the normal search (be aware that for this a lot of space on the hard-disc might be requiered).
If there is no chance to obtain negative results in appropriate time, use the DFS-like search to obtain at
least some drawings. For both approaches and for each of the 5 graph classes there is a java-class

        (DFS)Test*ClassName*

which is used to execute the creation of drawings.


--------
NORMAL SEARCH: mainOpt
--------

Parameters are:

- **CLASS_NAME**: name of the graph class (there is no need to change this).

- **USE_GAPS**: it is only true for the gap-planar graphs (do not change this).

- **STOP_AFTER**: can be used to stop the creation of new drawings after **STOP_AFTER** drawings were created
(counted for the drawings created for K_{**END_INDEX**} or the graph corresponding to the last Tuple in 'biIndices' respectively);
in this case a temp-file is created and saved with the current state of the calculations.

- **TEMP_AVAILABLE**: set it to true, if a temp-file was created before (the **START_INDEX** or
the first Tuple in 'biIndices' should correspond to the data in the temp-file; i.e., if
the temp-file was saved for K_{4,5}, then the first entry of biIndices should be
'new Tuple(4,5)').

- **START_INDEX** and **END_INDEX**: All drawings for the complete graphs K_{**START_INDEX**} to K_{**END_INDEX**} 
are created and saved; it is expected that the drawings for K_{**START_INDEX**} were already calculated.

- **INDICES**: This gives the order, in which complete bipartite graphs are calculated. For a graph
K_{a,b}, the first entry of a Tuple gives the 'a', and the second gives the 'b'.
It is expected that the drawings for the graph corresponding to the first entry of **INDICES**
have already been calculated. Note that two successive Tuples must differ by one in exactly one
entry (for example, if there is a Tuple with entries (4,5), the next Tuple must either be (4,6) or (5,5)).


For the k-planar graphs there is an additional parameter

- **PLANARITY**: This is for the 'k', that is, if you want to calculate the drawings for 2-planar graphs,
set this parameter to 2.

Procedure calls in the 'main'-procedure of these classes:

- test.initComplete(): The drawing of K_3 is created and saved on hard-disc.

- test.testComplete(): The drawings are created for the complete graphs K_{**START_INDEX**} to K_{**END_INDEX**} (may be time consuming).

- test.prepareDrawingComplete(*index*): Prepares the drawings of K_{*index*} (calculate the positions and labels of the vertices und
the labels of the edges).

- test.drawComplete(*index*): Show the drawings for K_{*index*} in a window (before this, execute test.prepareDrawingComplete(index))

- test.initBipartite(): The 2 drawings of K_{2,2} are created and saved on hard-disc.

- test.testBipartite(): The drawings are created for the complete bipartite graphs corresponding to the Tuples
in 'biIndices' (may be time consuming).

- test.prepareDrawingBipartite(*index1*, *index2*): Prepares the drawings of K_{*index1*, *index2*} (calculate the positions
and labels of the vertices und the labels of the edges).

- test.drawBipartite(*index1*, *index2*): Show the drawings for K_{*index1*, *index2*} in a window (before this, execute
test.prepareDrawingBipartite(*index1*, *index2*))


--------
SEARCH WITH MORE INFORMATION: mainExt
--------
The difference to mainOpt is that here all the drawings that where created (even the isomorphic ones) in the insertion process are stored. This type of search takes longer compared to the previous one, since there will be many accesses to the disc.


--------------------------------------
DFS-LIKE SEARCH: mainOpt
--------------------------------------
In this approach, it is tried to create drawings with as many vertices as possible.
Thereby it is startet from a base, that is from the drawings for a certain complete
or complete bipartite graph.
A drawings is saved, if it is not possible to add another vertex into this drawing,
and if there is no other drawing of this graph saved, that has the same number of crossings.

Parameters are:

- **CLASS_NAME**: name of the graph class (there is no need to change this)

- **USE_GAPS**: it is only true for the gap-planar graphs (do not change this)

- **START**: Number of base drawing to start with.

- **END**: Number of last base drawing to consider.

- **FINAL_INDEX1**: for the complete bipartite graphs calculate the drawings up to this index; that is,
if this parameter is set to 4, then vertices are alternatingly inserted into the two bipartite sets, until
set1 has 4 vertices; then vertices are only inserted into the other bipartite set; thus, it is tried to create
drawings of K_{4,b} with b as large as possible.


For the k-planar graphs there is an additional parameter

- **PLANARITY**: This is for the 'k', that is, if you want to calculate the drawings for 2-planar graphs,
set this parameter to 2.


Procedure calls in the 'main'-procedure of these classes:

- test.initComplete(): Base drawings are created and saved on hard disc. Base drawings are all the drawings of K_5.

- test.testComplete(): Drawings are searched for the base drawings with numbers **START** to **END** (may be very time consuming).

- test.prepareDrawingComplete(*index*): Prepares the saved drawings of K_{*index*}
(calculate the positions and labels of the vertices and the labels of the edges).

- test.drawComplete(*index*): Shows the saved drawings for K_{*index*} in a window
(before this, execute test.prepareDrawingComplete(*index*))

- test.initBipartite(): Base drawings are created and saved on hard disc. Base drawings are all the drawings for K_{3,3}.

- test.testBipartite(): Drawings are searched for the base drawings with numbers **START** to **END** (may be very time consuming).

- test.prepareDrawingBipartite(*index1*, *index2*): Prepares the saved drawings of K_{*index1*, *index2*}
(calculate the positions and labels of the vertices und the labels of the edges).

- test.drawBipartite(*index1*, *index2*): Show the saved drawings for K_{*index1*, *index2*} in a window
(before this, execute test.prepareDrawingBipartite(*index1*, *index2*))




--------------------------------------
EXPORT
--------------------------------------

Using the class 'RunExport' in the package 'io.export' you can export all files of a
certain folder (specified by 'folderName') to gml-files.
Note that there should not be to many drawings in this folder, in order to not get an error due to large array size.



--------------------------------------
CONSTANT
--------------------------------------
In the package 'data' you find a class 'Constant'. There you can change some parameters, mostly for printing information in the console.

- **INSERTING_PROGRESS**: Progress is printed after inserting a new vertex into this many drawnings.

- **INTERNAL_COMPARING_PROGRESS**: Progress is printed after testing this many drawings in RAM for isomorphism.

- **EXTERNAL_COMPARING_PROGRESS**: Progress is printed after testing this many drawings on hard disc for isomorphism.

- **DRAWING_PROGRESS**: Progress is printed after calculating positions & labels for this many drawings.

- **MIGRATION_PROGRESS**: Progress when changing the structure of the saved files.

- **EMBEDDING_RAM_LIST_SIZE**: newly created drawings are added to a list, until the list reaches this size; then the drawings
in the list are filtered for isomorphism and the remaining drawings are saved on hard disc.

- **DFS_PRINT**: Current state of the DFS-like search is printed after this many seconds.

- **WINDOW_WIDTH**: Width of the window shown when calling corresponding methods.

- **WINDOW_HEIGHT**: Height of the window shown when calling corresponding methods.

If one of the following parameters is changed, it is recommended to calculate the positions of the drawings anew.

- **CANVAS_WIDTH**: Width of the canvas where a drawing is shown.

- **CANVASW_HEIGHT**: Height of the canvas where a drawing is shown.

- **NODE_SIZE**: Width and height of the vertices in the window.
