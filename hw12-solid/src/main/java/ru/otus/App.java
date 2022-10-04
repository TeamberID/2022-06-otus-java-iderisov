package ru.otus;

import java.util.Arrays;
import java.util.HashMap;

public class App {

    public static void main(String[] args) {
        ATM betaATM = prepareATM();

//        betaATM.withdraw(1000000); // EXCEPTION

        betaATM.withdraw(49900);

        betaATM.deposit(prepareCash(3));

        betaATM.balance();

        betaATM.deposit(prepareCash(1000)); // EXCEPTION

    }

    private static ATM prepareATM() {
        HashMap<Denomination, BanknoteStack> banknoteStackHashMap = new HashMap<>();

        Arrays.stream(Denomination.values()).forEach(denomination -> {
            banknoteStackHashMap.put(denomination, new BanknoteStack(denomination, 10));
        });

        return new BetaATM(new Cash(banknoteStackHashMap));
    }

    private static Cash prepareCash(int amount) {
        HashMap<Denomination, BanknoteStack> banknoteStackHashMap = new HashMap<>();

        Arrays.stream(Denomination.values()).forEach(denomination -> {
            banknoteStackHashMap.put(denomination, new BanknoteStack(denomination, amount));
        });

        return new Cash(banknoteStackHashMap);
    }

}
