package com.java.lzhmzx;

import java.util.ArrayList;

public class NewsDataHelper {

    public static String text = "\"Material is the metaphor.\\n\\n\"\n" +
            "\n" +
            "        \"A material metaphor is the unifying theory of a rationalized space and a system of motion.\"\n" +
            "        \"The material is grounded in tactile reality, inspired by the study of paper and ink, yet \"\n" +
            "        \"technologically advanced and open to imagination and magic.\\n\"\n" +
            "        \"Surfaces and edges of the material provide visual cues that are grounded in reality. The \"\n" +
            "        \"use of familiar tactile attributes helps users quickly understand affordances. Yet the \"\n" +
            "        \"flexibility of the material creates new affordances that supercede those in the physical \"\n" +
            "        \"world, without breaking the rules of physics.\\n\"\n" +
            "        \"The fundamentals of light, surface, and movement are key to conveying how objects move, \"\n" +
            "        \"interact, and exist in space and in relation to each other. Realistic lighting shows \"\n" +
            "        \"seams, divides space, and indicates moving parts.\\n\\n\"\n" +
            "\n" +
            "        \"Bold, graphic, intentional.\\n\\n\"\n" +
            "\n" +
            "        \"The foundational elements of print based design typography, grids, space, scale, color, \"\n" +
            "        \"and use of imagery guide visual treatments. These elements do far more than please the \"\n" +
            "        \"eye. They create hierarchy, meaning, and focus. Deliberate color choices, edge to edge \"\n" +
            "        \"imagery, large scale typography, and intentional white space create a bold and graphic \"\n" +
            "        \"interface that immerse the user in the experience.\\n\"\n" +
            "        \"An emphasis on user actions makes core functionality immediately apparent and provides \"\n" +
            "        \"waypoints for the user.\\n\\n\"\n" +
            "\n" +
            "        \"Motion provides meaning.\\n\\n\"\n" +
            "\n" +
            "        \"Motion respects and reinforces the user as the prime mover. Primary user actions are \"\n" +
            "        \"inflection points that initiate motion, transforming the whole design.\\n\"\n" +
            "        \"All action takes place in a single environment. Objects are presented to the user without \"\n" +
            "        \"breaking the continuity of experience even as they transform and reorganize.\\n\"\n" +
            "        \"Motion is meaningful and appropriate, serving to focus attention and maintain continuity. \"\n" +
            "        \"Feedback is subtle yet clear. Transitions are efﬁcient yet coherent.\\n\\n\"\n" +
            "\n" +
            "        \"3D world.\\n\\n\"\n" +
            "\n" +
            "        \"The material environment is a 3D space, which means all objects have x, y, and z \"\n" +
            "        \"dimensions. The z-axis is perpendicularly aligned to the plane of the display, with the \"\n" +
            "        \"positive z-axis extending towards the viewer. Every sheet of material occupies a single \"\n" +
            "        \"position along the z-axis and has a standard 1dp thickness.\\n\"\n" +
            "        \"On the web, the z-axis is used for layering and not for perspective. The 3D world is \"\n" +
            "        \"emulated by manipulating the y-axis.\\n\\n\"\n" +
            "\n" +
            "        \"Light and shadow.\\n\\n\"\n" +
            "\n" +
            "        \"Within the material environment, virtual lights illuminate the scene. Key lights create \"\n" +
            "        \"directional shadows, while ambient light creates soft shadows from all angles.\\n\"\n" +
            "        \"Shadows in the material environment are cast by these two light sources. In Android \"\n" +
            "        \"development, shadows occur when light sources are blocked by sheets of material at \"\n" +
            "        \"various positions along the z-axis. On the web, shadows are depicted by manipulating the \"\n" +
            "        \"y-axis only. The following example shows the card with a height of 6dp.\\n\\n\"\n" +
            "\n" +
            "        \"Resting elevation.\\n\\n\"\n" +
            "\n" +
            "        \"All material objects, regardless of size, have a resting elevation, or default elevation \"\n" +
            "        \"that does not change. If an object changes elevation, it should return to its resting \"\n" +
            "        \"elevation as soon as possible.\\n\\n\"\n" +
            "\n" +
            "        \"Component elevations.\\n\\n\"\n" +
            "\n" +
            "        \"The resting elevation for a component type is consistent across apps (e.g., FAB elevation \"\n" +
            "        \"does not vary from 6dp in one app to 16dp in another app).\\n\"\n" +
            "        \"Components may have different resting elevations across platforms, depending on the depth \"\n" +
            "        \"of the environment (e.g., TV has a greater depth than mobile or desktop).\\n\\n\"\n" +
            "\n" +
            "        \"Responsive elevation and dynamic elevation offsets.\\n\\n\"\n" +
            "\n" +
            "        \"Some component types have responsive elevation, meaning they change elevation in response \"\n" +
            "        \"to user input (e.g., normal, focused, and pressed) or system events. These elevation \"\n" +
            "        \"changes are consistently implemented using dynamic elevation offsets.\\n\"\n" +
            "        \"Dynamic elevation offsets are the goal elevation that a component moves towards, relative \"\n" +
            "        \"to the component’s resting state. They ensure that elevation changes are consistent \"\n" +
            "        \"across actions and component types. For example, all components that lift on press have \"\n" +
            "        \"the same elevation change relative to their resting elevation.\\n\"\n" +
            "        \"Once the input event is completed or cancelled, the component will return to its resting \"\n" +
            "        \"elevation.\\n\\n\"\n" +
            "\n" +
            "        \"Avoiding elevation interference.\\n\\n\"\n" +
            "\n" +
            "        \"Components with responsive elevations may encounter other components as they move between \"\n" +
            "        \"their resting elevations and dynamic elevation offsets. Because material cannot pass \"\n" +
            "        \"through other material, components avoid interfering with one another any number of ways, \"\n" +
            "        \"whether on a per component basis or using the entire app layout.\\n\"\n" +
            "        \"On a component level, components can move or be removed before they cause interference. \"\n" +
            "        \"For example, a floating action button (FAB) can disappear or move off screen before a \"\n" +
            "        \"user picks up a card, or it can move if a snackbar appears.\\n\"\n" +
            "        \"On the layout level, design your app layout to minimize opportunities for interference. \"\n" +
            "        \"For example, position the FAB to one side of stream of a cards so the FAB won’t interfere \"\n" +
            "        \"when a user tries to pick up one of cards.\\n\\n\"";


    public static ArrayList<News> getDataExamples(){
        ArrayList<News> newsArrayList = new ArrayList<>();
        newsArrayList.add(new News("Dallas police HQ attack: Suspect believed killed during standoff",text,R.mipmap.news_one, false));
        newsArrayList.add(new News("Hugh Jackman says coffee can change the world",text,R.mipmap.news_two,true));
        newsArrayList.add(new News("Australia in charge of second Test against West Indies in Jamaica",text,R.mipmap.news_three,true));
        newsArrayList.add(new News("Sweden royal wedding","Australia in charge of second Test against West Indies in Jamaica",R.mipmap.news_four,false));
        return newsArrayList;
    }
}
