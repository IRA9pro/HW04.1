import java.util.Random;

public class Homework {
    public static int bossHealth = 1500;
    public static int bossDamage = 50;
    public static String bossDefense;
    public static int isBossStunning;

    public static int[] heroesHealth = {290, 270, 250, 260, 400, 240, 230, 280};
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 0, 0, 15};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic",
            "Heal", "Golem", "Lucky", "Witcher", "Thor"};
    public static boolean isHeroLuckyDodged;

    public static int roundNumber;
    public static String critMessage;


    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }


    // Game Over
    public static boolean isGameOver () {
        if (bossHealth <= 0) {
            System.out.println("Heroes WON!!!");
            return true;
        }

        boolean allHearoesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHearoesDead = false;
                break;
            }
        }
        if (allHearoesDead) {
            System.out.println("Boss WON!!!");
            return true;
        }

        return false;
    }


    // Boss Defense
    public static void chooseBossDefense(){
        Random random  = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefense = heroesAttackType[randomIndex];
    }


    // Rounds
    public static void playRound() {
        roundNumber++;
        chooseBossDefense();
        bossAttack();
        heroesAttack();
        printStatistics();
        healHeroes();
    }


    // Heroes Attacks
    public static void heroesAttack() {
        critMessage = null;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (heroesAttackType[i] == "Thor"){
                    Random random1 = new Random();
                    int stun = random1.nextInt(2);
                    if (stun == 0) isBossStunning = 2;
                }

                int damage  = heroesDamage[i];
                if (bossDefense == heroesAttackType[i]) {
                    Random random2 = new Random();
                    int coef = random2.nextInt(9) + 2;
                    damage *= coef;
                    critMessage = "Critical damage: " + damage;
                }

                bossHealth -= damage;
                if (bossHealth < 0) bossHealth = 0;
            }
        }
    }

    // Boss Attacks
    public static void bossAttack() {
        if (isBossStunning == 1) return;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesAttackType[i] == "Lucky") {
                    Random random = new Random();
                    int dodged = random.nextInt(4);
                    if (dodged == 0) {
                        isHeroLuckyDodged = true;
                        continue;
                    }
                }
                if (heroesAttackType[i] != "Golem" && heroesHealth[4] > 0) {
                    heroesHealth[4] -= (bossDamage / 5);
                    heroesHealth[i] -= (bossDamage * 4 / 5);
                    continue;
                }
                heroesHealth[i] -= bossDamage;
                if (heroesHealth[i] < 0) heroesHealth[i] = 0;
            }
        }
    }

    // Heals && Respawn
    public static void healHeroes() {
        int indexHeal = -1, indexRespawn = -1;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] == 0 && heroesHealth[6] > 0 && indexRespawn < 0) {
                heroesHealth[i] = heroesHealth[6];
                heroesHealth[6] = 0;
                indexRespawn = i;
            }
            if (heroesAttackType[i] == "Heal" || heroesHealth[i] == 0) continue;
            if (heroesHealth[i] < 100 &&
                    (indexHeal == -1 || heroesHealth[i] < heroesHealth[indexHeal])) {
                indexHeal = i;
            }
        }

        if (indexHeal >= 0) {
            heroesHealth[indexHeal] += 20;
            System.out.println("Medic heals: " + heroesAttackType[indexHeal]);
        }
        if (indexRespawn >= 0) {
            System.out.println("Wither respawns: " + heroesAttackType[indexRespawn]);
        }

    }


    // Statistics
    public static void printStatistics() {
        System.out.println("Round " + roundNumber + "----------");

        /*
        String defense;
        if (bossDefense == null) defense = "No deafens";
        else defense = bossDefence;
         */

        if (isBossStunning == 1) System.out.println("Boss Health: " + bossHealth +
                    " STUNNED " +
                    " Defence: " + (bossDefense == null ? " No defense" : bossDefense));
        else System.out.println("Boss Health: " + bossHealth +
                " Damage: " + bossDamage +
                " Defence: " + (bossDefense == null ? " No defense" : bossDefense));

        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesDamage[i] == 0) {
                if (isHeroLuckyDodged && heroesAttackType[i] == "Lucky") {
                    System.out.println(heroesAttackType[i] +
                            " Health: " + heroesHealth[i] +
                            " Dodged");
                } else {
                    System.out.println(heroesAttackType[i] + " Health: " + heroesHealth[i]);
                    continue;
                }
            } else if (heroesAttackType[i] == "Thor" && isBossStunning > 1) {
                System.out.println(heroesAttackType[i] +
                        " Health: " + heroesHealth[i] +
                        " Damage: " + heroesDamage[i] +
                        " Made Stun");
            } else {
                if (heroesHealth[i] == 0) System.out.println(heroesAttackType[i] +
                        " Health: " + heroesHealth[i]);
                else System.out.println(heroesAttackType[i] +
                        " Health: " + heroesHealth[i] +
                        " Damage: " + heroesDamage[i]);
            }
        }

        isHeroLuckyDodged = false;
        if (isBossStunning > 0) isBossStunning--;

        if (critMessage != null) System.out.println(critMessage);
    }
}
