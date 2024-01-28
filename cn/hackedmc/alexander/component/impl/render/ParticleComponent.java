package cn.hackedmc.alexander.component.impl.render;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.Priorities;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.render.Render2DEvent;
import cn.hackedmc.alexander.util.render.particle.Particle;

import java.util.concurrent.ConcurrentLinkedQueue;

@Rise
public class ParticleComponent extends Component {

    public static ConcurrentLinkedQueue<Particle> particles = new ConcurrentLinkedQueue<>();
    public static int rendered;

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<Render2DEvent> onRender2DEvent = event -> {
        if (particles.isEmpty()) return;
        NORMAL_POST_RENDER_RUNNABLES.add(ParticleComponent::render);
    };

    public static void render() {
        if (mc.ingameGUI.frame != rendered) {
            particles.forEach(particle -> {
                particle.render();

                if (particle.time.getElapsedTime() > 50 * 3 * 20) {
                    particles.remove(particle);
                }
            });

            threadPool.execute(() -> {
                particles.forEach(Particle::update);
            });

            rendered = mc.ingameGUI.frame;
        }
    }

    public static void add(final Particle particle) {
        particles.add(particle);
    }
}
