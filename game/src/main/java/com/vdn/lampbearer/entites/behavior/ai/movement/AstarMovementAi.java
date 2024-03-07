package com.vdn.lampbearer.entites.behavior.ai.movement;

import com.vdn.lampbearer.game.world.World;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;

import java.util.*;

/**
 * A* algorithm
 *
 * @author Chizhov D. on 2024.03.07
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AstarMovementAi extends MovementAi {

    private static AstarMovementAi INSTANCE;


    public static AstarMovementAi getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new AstarMovementAi());
    }


    @Override
    protected Optional<ArrayList<Position3D>> createPath(Position3D from,
                                                         Position3D to,
                                                         World world) {
        Queue<Node> openSet = new PriorityQueue<>();
        Map<Position3D, Node> allNodes = new HashMap<>();

        Node start = new Node(from, null, 0d, computeCost(from, to));
        openSet.add(start);
        allNodes.put(from, start);

        while (!openSet.isEmpty()) {
            Node next = openSet.poll();
            if (next.current.equals(to)) {
                ArrayList<Position3D> route = new ArrayList<>();
                Node current = next;

                do {
                    route.add(0, current.current);
                    current = allNodes.get(current.previous);
                } while (current != null);

                return Optional.of(route);
            }

            world.getWalkablePositionsAround(next.current).forEach(p -> {
                Node nextNode = allNodes.getOrDefault(p, new Node(p));
                allNodes.put(p, nextNode);

                double newScore = next.routeScore + computeCost(next.current, p);
                if (newScore < nextNode.routeScore) {
                    nextNode.previous = next.current;
                    nextNode.routeScore = newScore;
                    nextNode.estimatedScore = newScore + computeCost(p, to);
                    openSet.add(nextNode);
                }
            });
        }

        return Optional.empty();
    }


    private double computeCost(Position3D from, Position3D to) {
        return Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY());
    }


    private static class Node implements Comparable<Node> {

        private final Position3D current;
        private Position3D previous;
        private double routeScore;
        private double estimatedScore;


        Node(Position3D current) {
            this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }


        Node(Position3D current, Position3D previous, double routeScore, double estimatedScore) {
            this.current = current;
            this.previous = previous;
            this.routeScore = routeScore;
            this.estimatedScore = estimatedScore;
        }


        @Override
        public int compareTo(Node other) {
            return Double.compare(this.estimatedScore, other.estimatedScore);
        }
    }
}
