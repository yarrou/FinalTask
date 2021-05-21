package service;

import building.Elevator;

import java.util.LinkedList;
import java.util.Queue;

public class Dispatcher {
    private Queue<Goal> goals;

    public Dispatcher(){
        goals = new LinkedList<>();
    }
    public Queue<Goal> getGoals(){
        return goals;
    }
    public void addGoal(Goal goal){
        goals.add(goal);
    }
    public Goal getGoal(){
        return goals.poll();
    }
    public boolean isThereGoal(){
        return goals.size()>0?true:false;
    }
}
