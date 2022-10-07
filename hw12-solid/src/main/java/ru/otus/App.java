package ru.otus;

import java.util.Arrays;
import java.util.HashMap;

public class App {

    public static void main(String[] args) {
        Atm betaAtm = prepareAtm();

//        betaATM.withdraw(1000000); // EXCEPTION

        betaAtm.withdraw(49900);

        betaAtm.deposit(prepareCash(3));

        betaAtm.balance();

        betaAtm.deposit(prepareCash(1000)); // EXCEPTION

    }

    private static Atm prepareAtm() {
        HashMap<Denomination, BanknoteStack> banknoteStackHashMap = new HashMap<>();

        Arrays.stream(Denomination.values()).forEach(denomination -> {
            banknoteStackHashMap.put(denomination, new BanknoteStack(denomination, 10));
        });

        return new BetaAtm(new Cash(banknoteStackHashMap));
    }

    private static Cash prepareCash(int amount) {
        HashMap<Denomination, BanknoteStack> banknoteStackHashMap = new HashMap<>();

        Arrays.stream(Denomination.values()).forEach(denomination -> {
            banknoteStackHashMap.put(denomination, new BanknoteStack(denomination, amount));
        });

        return new Cash(banknoteStackHashMap);
    }

}
